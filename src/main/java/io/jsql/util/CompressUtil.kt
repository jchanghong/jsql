package io.jsql.util

import com.google.common.collect.Lists
import io.jsql.mysql.MySQLMessage

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.ArrayList
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.zip.Deflater
import java.util.zip.Inflater


/**
 * 压缩数据包协议
 *
 *
 * http://dev.mysql.com/doc/internals/en/compressed-packet-header.html
 *
 *
 * (包头)
 * 3 Bytes   压缩长度
 * 1 Bytes   压缩序列号
 * 3 Bytes   压缩前的长度
 *
 *
 * (包体)
 * n Bytes   压缩内容 或 未压缩内容
 *
 *
 * | -------------------------------------------------------------------------------------- |
 * | comp-length  |  seq-id  | uncomp-len   |                Compressed Payload             |
 * | ------------------------------------------------ ------------------------------------- |
 * |  22 00 00    |   00     |  32 00 00    | compress("\x2e\x00\x00\x00\x03select ...")    |
 * | -------------------------------------------------------------------------------------- |
 *
 *
 * Q:为什么消息体是 压缩内容 或者未压缩内容?
 * A:这是因为mysql内部有一个约定，如果查询语句payload小于50字节时， 对内容不压缩而保持原貌的方式，而mysql此举是为了减少CPU性能开销
 */
object CompressUtil {

    val MINI_LENGTH_TO_COMPRESS = 50
    val NO_COMPRESS_PACKET_LENGTH = MINI_LENGTH_TO_COMPRESS + 4


    /*
      压缩数据包
	  @param input
	 * @param con
	 * @param compressUnfinishedDataQueue
	 * @return
	 */
    //	public static ByteBuffer compressMysqlPacket(ByteBuffer input, AbstractConnection con,
    //			ConcurrentLinkedQueue<byte[]> compressUnfinishedDataQueue) {
    //
    //		byte[] byteArrayFromBuffer = getByteArrayFromBuffer(input);
    //		con.recycle(input);
    //
    //		byteArrayFromBuffer = mergeBytes(byteArrayFromBuffer, compressUnfinishedDataQueue);
    //		return compressMysqlPacket(byteArrayFromBuffer, con, compressUnfinishedDataQueue);
    //	}
    //
    //
    /*
	  压缩数据包
	  @param data
	 * @param con
	 * @param compressUnfinishedDataQueue
	 * @return
	 */
    //	private static ByteBuffer compressMysqlPacket(byte[] data, AbstractConnection con,
    //			ConcurrentLinkedQueue<byte[]> compressUnfinishedDataQueue) {
    //
    //		ByteBuffer byteBuf = con.allocate();
    //		byteBuf = con.checkWriteBuffer(byteBuf, data.length, false); //TODO: 数据量大的时候, 此处是是性能的堵点
    //
    //		MySQLMessage msg = new MySQLMessage(data);
    //		while ( msg.hasRemaining() ) {
    //
    //			//包体的长度
    //			int packetLength = 0;
    //
    //			//可读的长度
    //			int readLength = msg.length() - msg.position();
    //			if ( readLength > 3 ) {
    //				packetLength = msg.readUB3();
    //				msg.move(-3);
    //			}
    //
    //			//校验数据包完整性
    //			if ( readLength < packetLength + 4 ) {
    //				byte[] packet = msg.readBytes(readLength);
    //				if (packet.length != 0) {
    //					compressUnfinishedDataQueue.add(packet);		//不完整的包
    //				}
    //			} else {
    //
    //				byte[] packet = msg.readBytes(packetLength + 4);
    //				if ( packet.length != 0 ) {
    //
    //					if ( packet.length <= NO_COMPRESS_PACKET_LENGTH ) {
    //						BufferUtil.writeUB3(byteBuf, packet.length);    //压缩长度
    //						byteBuf.put(packet[3]);			 		 		//压缩序号
    //						BufferUtil.writeUB3(byteBuf, 0);  				//压缩前的长度设置为0
    //						byteBuf.put(packet);							//包体
    //
    //					} else {
    //
    //						byte[] compress = compress(packet);				//压缩
    //
    //						BufferUtil.writeUB3(byteBuf, compress.length);
    //						byteBuf.put(packet[3]);
    //						BufferUtil.writeUB3(byteBuf, packet.length);
    //						byteBuf.put(compress);
    //					}
    //				}
    //			}
    //		}
    //		return byteBuf;
    //	}

    /**
     * 解压数据包,同时做分包处理

     * @param data
     * *
     * @param decompressUnfinishedDataQueue
     * *
     * @return
     */
    fun decompressMysqlPacket(data: ByteArray,
                              decompressUnfinishedDataQueue: ConcurrentLinkedQueue<ByteArray>): List<ByteArray> {

        val msg = MySQLMessage(data)

        //包头
        //-----------------------------------------
        val packetLength = msg.readUB3()  //压缩的包长
        val packetId = msg.read()           //压缩的包号
        val oldLen = msg.readUB3()           //压缩前的长度

        //未压缩, 直接返回
        if (packetLength == data.size - 4) {
            var arg:ByteArray?=null
            return Lists.newArrayList<ByteArray>(arg)

            //压缩不成功的, 直接返回
        } else if (oldLen == 0) {
            val readBytes = msg.readBytes()
            return splitPack(readBytes, decompressUnfinishedDataQueue)

            //解压
        } else {
            val de = decompress(data, 7, data.size - 7)
            return splitPack(de, decompressUnfinishedDataQueue)
        }
    }

    /**
     * 分包处理

     * @param in
     * *
     * @param decompressUnfinishedDataQueue
     * *
     * @return
     */
    private fun splitPack(`in`: ByteArray, decompressUnfinishedDataQueue: ConcurrentLinkedQueue<ByteArray>): List<ByteArray> {
        var `in` = `in`

        //合并
        `in` = mergeBytes(`in`, decompressUnfinishedDataQueue)

        val smallPackList = ArrayList<ByteArray>()

        val msg = MySQLMessage(`in`)
        while (msg.hasRemaining()) {

            val readLength = msg.length() - msg.position()
            var packetLength = 0
            if (readLength > 3) {
                packetLength = msg.readUB3()
                msg.move(-3)
            }

            if (readLength < packetLength + 4) {
                val packet = msg.readBytes(readLength)
                if (packet.size != 0) {
                    decompressUnfinishedDataQueue.add(packet)
                }

            } else {
                val packet = msg.readBytes(packetLength + 4)
                if (packet.size != 0) {
                    smallPackList.add(packet)
                }
            }
        }

        return smallPackList
    }

    /**
     * 合并 解压未完成的字节
     */
    private fun mergeBytes(`in`: ByteArray, decompressUnfinishedDataQueue: ConcurrentLinkedQueue<ByteArray>): ByteArray {
        var `in` = `in`

        if (!decompressUnfinishedDataQueue.isEmpty()) {

            val out = ByteArrayOutputStream()
            try {
                while (!decompressUnfinishedDataQueue.isEmpty()) {
                    out.write(decompressUnfinishedDataQueue.poll())
                }
                out.write(`in`)
                `in` = out.toByteArray()

            } catch (e: IOException) {
                throw RuntimeException(e)
            } finally {
                try {
                    out.close()
                } catch (ignored: IOException) {
                }

            }
        }
        return `in`
    }

    private fun getByteArrayFromBuffer(byteBuf: ByteBuffer): ByteArray {
        byteBuf.flip()
        val row = ByteArray(byteBuf.limit())
        byteBuf.get(row)
        byteBuf.clear()
        return row
    }

    fun compress(byteBuf: ByteBuffer): ByteArray {
        return compress(getByteArrayFromBuffer(byteBuf))
    }

    /**
     * 适用于mysql与客户端交互时zlib 压缩

     * @param data
     * *
     * @return
     */
    fun compress(data: ByteArray): ByteArray {

        var output: ByteArray? = null

        val compresser = Deflater()
        compresser.setInput(data)
        compresser.finish()

        val out = ByteArrayOutputStream(data.size)
        val result = ByteArray(1024)
        try {
            while (!compresser.finished()) {
                val length = compresser.deflate(result)
                out.write(result, 0, length)
            }
            output = out.toByteArray()
        } finally {
            try {
                out.close()
            } catch (ignored: Exception) {
            }

            compresser.end()
        }

        return output!!
    }

    /**
     * 适用于mysql与客户端交互时zlib解压

     * @param data 数据
     * *
     * @param off  偏移量
     * *
     * @param len  长度
     * *
     * @return
     */
    fun decompress(data: ByteArray, off: Int, len: Int): ByteArray {

        var output: ByteArray? = null

        val decompresser = Inflater()
        decompresser.reset()
        decompresser.setInput(data, off, len)

        val out = ByteArrayOutputStream(data.size)
        try {
            val result = ByteArray(1024)
            while (!decompresser.finished()) {
                val i = decompresser.inflate(result)
                out.write(result, 0, i)
            }
            output = out.toByteArray()
        } catch (e: Exception) {
            throw RuntimeException(e)
        } finally {
            try {
                out.close()
            } catch (ignored: Exception) {
            }

            decompresser.end()
        }
        return output!!
    }

}

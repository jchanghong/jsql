package io.jsql.storage

import java.io.IOException

/**
 * Created by 长宏 on 2016/12/17 0017.
 */
class StorageException : IOException {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to [.initCause].

     * @param message the detail message. The detail message is saved for                later retrieval by the [.getMessage] method.
     */
    constructor(message: String) : super(message) {}

    constructor(cause: Throwable) : super(cause) {}
}

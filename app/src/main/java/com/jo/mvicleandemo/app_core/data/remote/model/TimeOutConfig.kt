package com.jo.mvicleandemo.app_core.data.remote.model

enum class TimeOutConfig {
    NORMAL(60 , 30, 15),
    UPLOADING(5 * 60 , 30 * 60 , 15 * 60 );

    // custom properties with default values
    var connectionTimeout: Long = 60
    var readTimeout: Long = 30
    var writeTimeout: Long = 15


    constructor()

    // custom constructors
    constructor(
        _connectionTimeout: Long, _readTimeout: Long, _writeTimeout: Long
    ) {
        this.connectionTimeout = _connectionTimeout
        this.readTimeout = _readTimeout
        this.writeTimeout = _writeTimeout
    }
}

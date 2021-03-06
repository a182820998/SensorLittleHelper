package com.example.user.soil_supervise_kotlin.dto

import android.content.Context
import com.example.user.soil_supervise_kotlin.models.AppSettingModel

class PhpUrlDto constructor(context: Context)
{
    private val _appSettingModel = AppSettingModel(context)
    private val _username = _appSettingModel.Username()
    private val _password = _appSettingModel.Password()
    private val _serverIp = _appSettingModel.ServerIp()

    fun LoadingHistoryDataById(id1 : String, id2 : String) : String
    {
        return "http://$_serverIp/load_history.php?&server=$_serverIp&user=$_username&pass=$_password&id=$id1&id2=$id2"
    }

    fun DeletedDataById(id1: String, id2: String) : String
    {
        return "http://$_serverIp/deleted_json.php?&server=$_serverIp&user=$_username&pass=$_password&id=$id1&id2=$id2"
    }

    fun ConnectDbByLogin(username: String, password: String) : String
    {
        return "http://$_serverIp/conn_json.php?&server=$_serverIp&user=$username&pass=$password"
    }

    fun EditingSensorQuantityByQuery(query: String, sensorId : String) : String
    {
        return "http://$_serverIp/$query.php?&server=$_serverIp&user=$_username&pass=$_password&sensor_id=$sensorId"
    }

    val LoadingWholeData = "http://$_serverIp/android_mysql.php?&server=$_serverIp&user=$_username&pass=$_password"
    val CleanDatabase = "http://$_serverIp/clean_db.php?&server=$_serverIp&user=$_username&pass=$_password"
    val LoadingLastData = "http://$_serverIp/android_mysql_last.php?&server=$_serverIp&user=$_username&pass=$_password"
}
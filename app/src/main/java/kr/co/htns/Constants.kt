package kr.co.htns

val BASE_URL = getBaseURL()

fun getBaseURL():String{
    return if(!BuildConfig.DEBUG)
        BuildConfig.url  //운영 서버 - build.gradle에서 적용
    else{
        when(BuildConfig.option){ //테스트 서버
            "main"   ->   "http://211.196.153.228:8893"

            else     -> ""
        }
    }
}

const val PREFERENCES_FILE_KEY = "settings_preferences"
const val PREFERENCES_SAVE_INFO = "settings_save_info"
const val PREFERENCES_SAVE_ID = "settings_save_id"
const val PREFERENCES_SAVE_PW = "settings_save_pw"
const val PREFERENCES_AUTOID = "settings_auto_id"
const val PREFERENCES_AUTOLOGIN = "settings_auto_login"
const val PREFERENCES_UPDATEAPP = "settings_update_app"
const val PREFERENCES_LOGIN_TYPE = "settings_login_type"
const val PREFERENCES_NAVI = "settings_navi"

const val INTENT_KEY_LOCATION_PARAM = "locationData"
const val INTENT_KEY_CLICK_NOTI = "clickNoti"
const val PREF_KEY_ENTP_SEQ = "enptSeq"
const val PREF_KEY_WORK_SEQ = "workSeq"
const val PREF_KEY_RES_SEQ = "resSeq"
const val NOTIFICATION_CHANNEL_LOCATION = "chLocation"
const val NOTIFICATION_CHANNEL_WORK = "chWork"
const val NOTIFICATION_CHANNEL_ALRAM = "alram"
const val PREF_KEY_USER_SEQ = "userSeq"
const val NOTIFICATION__CHANNEL_GROUP_ID = "chGroup"
const val ACTION_START_LOCATION_SERVICE = "startLocationService"
const val ACTION_STOP_LOCATION_SERVICE = "stopLocationService"
const val TRANSITIONS_RECEIVER_ACTION = "transitions"


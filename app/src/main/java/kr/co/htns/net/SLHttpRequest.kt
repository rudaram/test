package kr.co.htns.net

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.net.URLEncoder
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class SLHttpRequest(private val urlString: String) {
    enum class RequestType {
        GET, POST, MULTIPART
    }

    private var boundary: String? = null
    private var requestType = RequestType.GET
    private val charSet = "UTF-8"
    private val parameters: HashMap<String, Any>
    protected var isSuccess = false
    protected var resultData: StringBuffer? = null
    protected var responseListener: OnResponseListener? = null
    private val mContext: Context? = null

    /**
     * 요청 전송
     */
    fun send(onResponseListener: OnResponseListener?) {
        responseListener = onResponseListener
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        HttpRequestTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        //        else
//            new HttpRequestTask().execute();
    }

    /**
     * 실제 요청 전송
     */
    protected fun doSend() {
        isSuccess = true
        resultData = StringBuffer()
        if (requestType == RequestType.GET) {
            var urlConnection: URLConnection? = null
            var scanner: Scanner? = null
            try {
                val url = URL(urlString + makeParamString(true))
                urlConnection = url.openConnection()
                if (urlString.startsWith("https://")) {

                    // SSL로 세팅해줘야 한다.
                    val trustAllCerts = arrayOf<TrustManager>(
                        object : X509TrustManager {
                            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                                return null
                            }

                            override fun checkClientTrusted(
                                certs: Array<X509Certificate>,
                                authType: String
                            ) {
                            }

                            override fun checkServerTrusted(
                                certs: Array<X509Certificate>,
                                authType: String
                            ) {
                            }
                        })
                    val sc = SSLContext.getInstance("SSL")
                    sc!!.init(null, trustAllCerts, SecureRandom())

                    //SSLContext sc = SSLContext.getDefault();
                    //sc.init(null,null,null);
                    if (sc == null) return
                    urlConnection = url.openConnection() as HttpsURLConnection
                    (urlConnection as HttpsURLConnection?)!!.sslSocketFactory = sc.socketFactory
                } else if (urlString.startsWith("http://")) {
                    urlConnection = url.openConnection() as HttpURLConnection
                }
                urlConnection!!.useCaches = false
                scanner = Scanner(urlConnection.getInputStream())
                while (scanner.hasNext()) {
                    val str = scanner.nextLine()
                    resultData!!.append(str)
                }
                //				scanner.close();
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                scanner?.close()
            }
        } else if (requestType == RequestType.POST) {
            var urlConnection: URLConnection? = null
            var scanner: Scanner? = null
            var inputStream: InputStream? = null
            var dataOutputStream: DataOutputStream? = null
            try {
                val url = URL(urlString)
                //                urlConnection = url.openConnection();
                if (urlString.startsWith("https://")) {
                    val sslContext = SSLContext.getDefault() ?: return
                    urlConnection = url.openConnection() as HttpsURLConnection
                    (urlConnection as HttpsURLConnection?)!!.sslSocketFactory =
                        sslContext.socketFactory
                } else if (urlString.startsWith("http://")) {
                    urlConnection = url.openConnection() as HttpURLConnection
                }
                urlConnection!!.doOutput = true
                urlConnection.useCaches = false
                urlConnection.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded"
                )
                dataOutputStream = DataOutputStream(urlConnection.getOutputStream())
                dataOutputStream.writeBytes(makeParamString(false))
                dataOutputStream.flush()
                inputStream = urlConnection.getInputStream()
                scanner = Scanner(inputStream)
                while (scanner.hasNext()) {
                    val str = scanner.nextLine()
                    resultData!!.append(str)
                }
                //				scanner.close();
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                scanner?.close()
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        Log.d("driverApp", "IOException")
                    }
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close()
                    } catch (e: IOException) {
                        Log.d("driverApp", "IOException")
                    }
                }
            }
        } else if (requestType == RequestType.MULTIPART) {
            var httpConn: HttpURLConnection? = null
            var printWriter: PrintWriter? = null
            var outputStream: OutputStream? = null
            var bufferedReader: BufferedReader? = null
            try {
                boundary = "===" + System.currentTimeMillis() + "==="
                val url = URL(urlString)
                if (urlString.startsWith("https://")) {
                    val sslContext = SSLContext.getDefault() ?: return
                    httpConn = url.openConnection() as HttpsURLConnection
                    (httpConn as HttpsURLConnection?)!!.sslSocketFactory = sslContext.socketFactory
                } else if (urlString.startsWith("http://")) {
                    httpConn = url.openConnection() as HttpURLConnection
                }
                //                httpConn = (HttpsURLConnection) url.openConnection();
                httpConn!!.useCaches = false
                httpConn.doOutput = true
                httpConn.doInput = true

//            httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
                httpConn.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=" + boundary
                )
                outputStream = httpConn.outputStream
                printWriter = PrintWriter(OutputStreamWriter(outputStream, charSet), true)

                // 파라미터
                val iterator: Iterator<String> = parameters.keys.iterator()
                while (iterator.hasNext()) {
                    val key = iterator.next()
                    val value = parameters[key]
                    val className = value!!.javaClass.simpleName
                    if (className == "Integer" || className == "Float" || className == "Double" || className == "String") {
                        // 파라미터가 숫자일 경우
                        // 파라미터가 문자열일 경우
                        printWriter.append("--" + boundary).append(LINE_FEED)
                        printWriter.append("Content-Disposition: form-data; name=\"$key\"").append(
                            LINE_FEED
                        )
                        printWriter.append("Content-Type: text/plain; charset=" + charSet).append(
                            LINE_FEED
                        )
                        printWriter.append(LINE_FEED)
                        printWriter.append(value.toString()).append(LINE_FEED)
                        printWriter.flush()
                    } else if (className == "FileParam") {
                        // 파라미터가 파일일 경우
                        val fileParam = value as FileParam?
                        printWriter.append("--" + boundary).append(LINE_FEED)
                        printWriter.append("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileParam!!.filename + "\"")
                            .append(
                                LINE_FEED
                            )
                        printWriter.append("Content-Type: " + fileParam.fileType).append(LINE_FEED)
                        printWriter.append("Content-Transfer-Encoding: binary").append(LINE_FEED)
                        printWriter.append(LINE_FEED)
                        printWriter.flush()
                        outputStream.write(fileParam.data)
                        printWriter.append(LINE_FEED)
                        outputStream.flush()
                    }
                }

//				printWriter.append(this.LINE_FEED).flush();
                printWriter.append("--" + boundary + "--").append(LINE_FEED)
                printWriter.close()

                // 응답 처리
                val status = httpConn.responseCode
                if (status == HttpURLConnection.HTTP_OK) {
                    bufferedReader = BufferedReader(InputStreamReader(httpConn.inputStream))
                    var line: String? = null
                    while (bufferedReader.readLine().also { line = it } != null) {
                        resultData!!.append(line)
                    }
                    bufferedReader.close()
                }
                httpConn.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpConn?.disconnect()
                printWriter?.close()
                if (outputStream != null) {
                    try {
                        outputStream.close()
                    } catch (e: IOException) {
                        Log.d("driverApp", "IOException")
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e: IOException) {
                        Log.d("driverApp", "IOException")
                    }
                }
            }
        }
    }

    /**
     * 응답 처리
     */
    protected fun doResponse() {
        if (responseListener != null) {
            if (isSuccess) {
                responseListener!!.OnSuccess(resultData.toString())
            } else {
                responseListener!!.OnFail(0, "")
            }
        }
    }

    /**
     * 요청 타입 설정
     *
     * @param requestType
     */
    fun setRequestType(requestType: RequestType) {
        this.requestType = requestType
    }

    /**
     * 요청 파라미터 한 개 항목 추가
     * 처리가능한 클래스 타입은 Integer,Double,Float,String
     * 파일의 경우 FileParam클래스를 반드시 사용
     *
     * @param name
     * @param value
     */
    fun addParameter(name: String, value: Any) {
        parameters[name] = value
    }

    /**
     * 요청 파라미터 묶음 추가
     *
     * @param params
     */
    fun addParameters(params: HashMap<String, Any>?) {
        parameters.putAll(params!!)
    }

    /**
     * 파라미터에 파일 추가시 사용
     */
    class FileParam {
        var filename: String? = null
        var fileType // 파일 타입 URLConnection.guessContentTypeFromName(fileName) 이걸로 되는듯
                : String? = null
        lateinit var data :ByteArray
    }

    /**
     * 파라미터 문자열 생성 (?name=value&...)
     *
     * @param urlEncode 인코딩 여부
     * @return
     */
    protected fun makeParamString(urlEncode: Boolean): String {
        val stringBuffer = StringBuffer()
        val iterator: Iterator<String> = parameters.keys.iterator()
        while (iterator.hasNext()) {
            val key = iterator.next()
            val value = parameters[key] ?: continue
            if (stringBuffer.length == 0) stringBuffer.append("?") else stringBuffer.append("&")
            if (urlEncode) {
                var encodeString: String? = null
                try {
                    encodeString = URLEncoder.encode(value.toString(), charSet)
                    stringBuffer.append("$key=$encodeString")
                } catch (e: UnsupportedEncodingException) {
                    Log.d("driverApp", "UnsupportedEncodingException")
                }
            } else {
                stringBuffer.append("$key=$value")
            }
        }
        return stringBuffer.toString()
    }

    interface OnResponseListener {
        fun OnSuccess(data: String?)
        fun OnFail(errorCode: Int, errorMessage: String?)
    }


    protected inner class HttpRequestTask :
        AsyncTask<String?, String?, String?>() {
        override fun doInBackground(vararg params: String?): String? {
            doSend()
            return null
        }
        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            doResponse()
        }


    }

    companion object {
        private const val LINE_FEED = "\r\n"
    }

    init {
        parameters = HashMap()
    }
}
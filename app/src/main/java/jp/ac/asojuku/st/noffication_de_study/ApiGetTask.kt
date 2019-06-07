package jp.ac.asojuku.st.noffication_de_study

import android.os.AsyncTask
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiGetTask(callback: (String?) -> Unit) : AsyncTask<String, Unit, String>() {
    var callback = callback
    val server = "https://main-eve.ssl-lolipop.jp/noffication-de-study/api/"
    // val server = "http://eve.main.jp/noffication-de-study/api/"

    override fun doInBackground(vararg params: String): String {
        val api_name: String = params[0]

        val urlBuilder: HttpUrl.Builder

        if (params.size == 2) {
            var paramString: String = params[1]
            paramString = paramString.substring(1, paramString.length - 1)
            val keyValuePairs = paramString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val param: HashMap<String, String> = HashMap()
            for (pair in keyValuePairs) {
                val entry = pair.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                param.put(entry[0].trim { it <= ' ' }, entry[1].trim { it <= ' ' })
            }

            val api = server + api_name
            urlBuilder = HttpUrl.parse(api)!!.newBuilder()

            param.forEach { name, value -> urlBuilder.addQueryParameter(name, value) }
        } else {
            val api = server + api_name
            urlBuilder = HttpUrl.parse(api)!!.newBuilder()
        }

        val request = Request.Builder().url(urlBuilder.toString()).build()
        val okHttpClient = OkHttpClient.Builder().build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()

        val data = response.body()!!.string()

        return data
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        callback(result)
    }
}
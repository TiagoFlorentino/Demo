package com.example.demo.AsyncTasks

import android.os.AsyncTask
import com.example.demo.Objects.Gist
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

interface PublicGistsAsyncTaskInterface  {
    fun publicGistsAsyncTaskFinished(gists: Array<Gist>)
    fun publicGistsAsyncTaskFailed()
}

class  PublicGistsAsyncTask(publicGistsAsyncTaskInterface:PublicGistsAsyncTaskInterface) : AsyncTask<Void, Void, String>() {

    var publicGistsAsyncTaskInterface : PublicGistsAsyncTaskInterface? = null
    var gists : Array<Gist>? = null

    init {
        this.publicGistsAsyncTaskInterface = publicGistsAsyncTaskInterface
    }

    override fun doInBackground(vararg params: Void?): String? {
        val url = "https://api.github.com/gists/public"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        var responseString : String = ""
        var response : Response = client.newCall(request).execute()
        responseString = response.body()!!.string()
        this.gists = Gson().fromJson(responseString, Array<Gist>::class.java)
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(this.gists != null){
            this.publicGistsAsyncTaskInterface?.publicGistsAsyncTaskFinished(this.gists!!)
        }
        else{
            this.publicGistsAsyncTaskInterface?.publicGistsAsyncTaskFailed()
        }
    }

}
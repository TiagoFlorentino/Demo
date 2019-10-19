package com.example.demo.AsyncTasks

import android.os.AsyncTask
import com.example.demo.Objects.Gist
import com.example.demo.Objects.GistDetail
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

interface GistDetailAsyncTaskInterface  {
    fun gistDetailAsyncTaskFinished(gistDetail: GistDetail)
    fun gistDetailAsyncTaskFailed()
}

class  GistDetailAsyncTask(gistDetailAsyncTaskInterface: GistDetailAsyncTaskInterface, gist: Gist) : AsyncTask<Void, Void, String>() {

    var gistDetailAsyncTaskInterface : GistDetailAsyncTaskInterface? = null
    var gist : Gist? = null
    var gistDetail: GistDetail? = null

    init {
        this.gistDetailAsyncTaskInterface = gistDetailAsyncTaskInterface
        this.gist = gist
    }

    override fun doInBackground(vararg params: Void?): String? {
        val url = "https://api.github.com/gists/" + this.gist!!.id
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        var responseString : String = ""
        var response : Response = client.newCall(request).execute()
        responseString = response.body()!!.string()
        this.gistDetail = Gson().fromJson(responseString, GistDetail::class.java)
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if(this.gistDetail != null){
            this.gistDetailAsyncTaskInterface?.gistDetailAsyncTaskFinished(this.gistDetail!!)
        }
        else{
            this.gistDetailAsyncTaskInterface?.gistDetailAsyncTaskFailed()
        }
    }

}
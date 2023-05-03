package com.example.github_retrofit

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.github_retrofit.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RepositoryAdapter

    private val githubApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    var binding: ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = RepositoryAdapter()
        recyclerView.adapter=adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchButton = findViewById<Button>(R.id.searchBtn)
        searchButton.setOnClickListener {
            val username = findViewById<EditText>(R.id.UserNameEditText).text.toString()
            searchRepositories(username)

        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }


    private fun searchRepositories(username: String){

        if (!isNetworkAvailable()) {
            Toast.makeText(
                this,
                "No hay conexión a internet. Por favor, verifica tu conexión y vuelve a intentarlo.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        githubApi.getRepositorios(username).enqueue(object : Callback<List<Repository>>{
            override fun onResponse(call: Call<List<Repository>>, response: Response<List<Repository>>){
                try{
                if (response.isSuccessful){
                    val repositories = response.body()
                    Log.d("Repositories", repositories.toString())
                    repositories?.let {
                        adapter.setData(it)
                    }
                }else{
                    Log.d("MainActivity","Error: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}")
            }
            }

            override fun onFailure(call: Call<List<Repository>>,t:Throwable) {
                try{
                Log.d("MainActivity", "Error: ${t.message}")
            }catch (e: Exception) {
                    Log.e("MainActivity", "Error: ${e.message}")
                }
            }
        })
    }



}
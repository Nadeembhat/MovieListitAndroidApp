package `in`.ernb.diagnalwithkotlin

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.regex.Matcher
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    lateinit var mPosterList: ArrayList<DataModel>
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: PosterAdapter
    lateinit var searchView: SearchView
    lateinit var manager: GridLayoutManager
    var progressBar: ProgressBar?=null
    lateinit var assetFiles: ArrayList<String>
    lateinit var sharedPreferences: SharedPreferences
    lateinit var appPreferences: AppPreferences
    var orientation: Int = 0
    var FILE_COUNT: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById(R.id.recycler_view)
        progressBar = findViewById(R.id.progressBar)
        assetFiles = getAssetFileNames()
        manager = GridLayoutManager(applicationContext, 3)
        manager.reverseLayout = false
        mRecyclerView.layoutManager = manager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        setRecyclerView(assetFiles[FILE_COUNT])
        assetFiles.removeAt(FILE_COUNT)

        val obj = object : PosterAdapter.OnBottomReachedListener {
            override  fun onBottomReached(position: Int) {
                if (assetFiles.size != 0) {
                    val file = assetFiles[FILE_COUNT]
                    if (mPosterList.size == 20) {
                        loadMoreData(file, "Second")
                    } else if (mPosterList.size == 40) {
                        loadMoreData(file, "Third")
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "No More Data... Reached EOL!!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        mAdapter.setOnBottomReachedListener(obj)
    }



    fun setRecyclerView(api:String){
        mPosterList = readAssetFiles(api)
        mAdapter = PosterAdapter(mPosterList, getScreenHeight())
        mRecyclerView.adapter = mAdapter
    }

    private fun getScreenHeight(): Int {
        Log.e("Actionbar \t", "Height\t")
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val height = size.y
        Log.e("Actionbar \t", "Height\t$height")
        return if (height < 1000) {
            height / 3 - 60
        } else height / 3 - 87
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_itmes, menu)
        // Associate searchable configuration with the SearchView
        val searchManager =
            getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = "Romantic Comedy"
        // listening to search query text change
        searchView.setOnQueryTextListener(object :
           SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean { // filter recycler view when query submitted
                mAdapter.getFilter()!!.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean { // filter recycler view when text is changed
                mAdapter.getFilter()!!.filter(query)
                return false
            }
        })
        return true
    }

    fun loadMoreData(apiData:String, call:String){
        val model = readAssetFiles(apiData)
        Toast.makeText(applicationContext, "$call\tPage ", Toast.LENGTH_LONG).show()
        mPosterList.addAll(model)
        mRecyclerView.postDelayed(Runnable {
            mAdapter.notifyDataSetChanged()
        },1000)
        assetFiles.removeAt(FILE_COUNT)
    }

    fun readAssetFiles(file:String):ArrayList<DataModel>{
        var list:ArrayList<DataModel> = arrayListOf()
        val obj = Util.readJsonObjectFromAPI(applicationContext,file)
        val arrayitems = obj.getJSONObject("page").getJSONObject("content-items").getJSONArray("content")
        var i =0
        for (item in 0 until arrayitems.length()){
         var posterimage:String = arrayitems.getJSONObject(i).getString("poster-image")
            val p:Pattern = Pattern.compile("([0-9])")
            val m:Matcher = p.matcher(posterimage);
            val dataModel  = DataModel()
            if(m.find()){
                posterimage = posterimage.replace("[^0-9]".toRegex(),"")
                Log.e("Mainactivity","\tPosterimage\t"+posterimage)
                dataModel.name =( arrayitems.getJSONObject(i).getString("name"))
                dataModel.poster_image=(Integer.parseInt(posterimage))
            }
            else{
                dataModel.name = (arrayitems.getJSONObject(i).getString("name"))
                dataModel.poster_image = 0
            }
            list.add(dataModel)
            i++

        }
     return   list
    }




    fun getAssetFileNames():ArrayList<String>{
       var items : ArrayList<String> = arrayListOf()
        val assetManager = applicationContext.assets
            for(file in assetManager.list("").orEmpty()){
                if(file.contains(".json")){
                    items.add(file)
                }

            }
        Log.e("MainActivity\t","\tList of items in asset\t"+items);
             /*assetManager.list("")?.let {
                for (p2 in it) {
                    Log.e("MainActivity\t","\tList in asset\t"+p2);
                }
            }*/
        return items
    }
}

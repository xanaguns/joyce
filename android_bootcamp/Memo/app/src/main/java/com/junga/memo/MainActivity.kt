package com.junga.memo

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("StaticFieldLeak")
class MainActivity : AppCompatActivity(), OnDeleteListener {

    lateinit var db : MemoDatabase
    var memoList = listOf<MemoEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = MemoDatabase.getInstance(this)!!

        button_add.setOnClickListener {
            val memo = MemoEntity(null, edittext_memo.text.toString())
            edittext_memo.setText("")
            insertMemo(memo)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        getAllMemos()
    }

    fun insertMemo(memo: MemoEntity) {
        // 1. MainThread vs WorkerThread(Background Thread)

        val insertTask = object : AsyncTask<Unit,Unit,Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                db.memoDao().insert(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }

        insertTask.execute()
    }

    fun getAllMemos() {
        val getTask = object : AsyncTask<Unit,Unit,Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                memoList = db.memoDao().getAll()
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                setRecyclerView(memoList)
            }
        }

        getTask.execute()
    }

    fun deleteMemo(memo: MemoEntity) {
        val deleteTask = object : AsyncTask<Unit,Unit,Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                db.memoDao().delete(memo)
            }

            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                getAllMemos()
            }
        }

        deleteTask.execute()
    }

    fun setRecyclerView(memoList: List<MemoEntity>) {
        recyclerView.adapter = MyAdapter(this, memoList, this)
    }

    override fun onDeleteListener(memo: MemoEntity) {
        deleteMemo(memo)
    }
}
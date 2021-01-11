package github.sun5066.socketclient.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import github.sun5066.socketclient.model.ChatData

object CommonBindingAdapter {

    @BindingAdapter("setMarginLeft")
    @JvmStatic
    fun setMarginLeft(_view: View, _dimen: Float) {
        val layoutParams = _view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.leftMargin = _dimen.toInt()
        _view.layoutParams = layoutParams
    }

    @BindingAdapter("setOnClickListener")
    @JvmStatic
    fun setOnClickListener(_view: View, _listener: View.OnClickListener?) {
        Log.d("ㅋㅋㅋㅋ", "asdsadasdsdaasdsad")
        _listener?.let { _view.setOnClickListener(it) }
    }

    @BindingAdapter("setItem")
    @JvmStatic
    fun setItems(_recyclerView: RecyclerView, _items: MutableList<ChatData>?) {
        if (_recyclerView.adapter == null) {
            _recyclerView.adapter = ChatRecyclerAdapterV2(mutableListOf())
        }
        val chatRecyclerAdapter = _recyclerView.adapter as ChatRecyclerAdapterV2
        _items?.let { chatRecyclerAdapter.setList(it) }
        _recyclerView.scrollToPosition(chatRecyclerAdapter.itemCount - 1)
        chatRecyclerAdapter.notifyDataSetChanged()
    }
}
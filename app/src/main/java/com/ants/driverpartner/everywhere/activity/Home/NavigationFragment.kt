package com.ants.driverpartner.everywhere.activity.Home
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ants.driverpartner.everywhere.R

class NavigationFragment : Fragment(), NavigationAdapter.INavigation {


    var adapter: NavigationAdapter? = null
    private var rvNavigation: RecyclerView? = null
    private val TAG = NavigationFragment::class.java.simpleName

    fun updateProfileData() {
        if (adapter != null) {
            adapter!!.refreshAdapter(fillData())
        }
    }


    private fun fillData(): ArrayList<NavigationData> {
        val navigationDataArrayList = ArrayList<NavigationData>()
        val title_array_navigation = getResources().getStringArray(R.array.title_array_navigation)
        val drawable_array_navigation =
            getResources().obtainTypedArray(R.array.drawable_array_navigation)

        for (i in title_array_navigation.indices) {
            val navigationData = NavigationData()
            navigationData.name = title_array_navigation[i]
            navigationData.drawableId = drawable_array_navigation.getResourceId(i, -1)
            navigationDataArrayList.add(navigationData)
        }
        return navigationDataArrayList
    }


    private fun replaceFragment(position: Int) {
        (getActivity() as HomeActivity).replaceFragment(position)
        adapter!!.setSelected(position)
    }

    private fun setAdapter() {
        rvNavigation!!.setLayoutManager(LinearLayoutManager(getActivity()))

        adapter = NavigationAdapter(this)
        rvNavigation!!.setAdapter(adapter)
        adapter!!.refreshAdapter(fillData())
    }

    fun changeRowSelector(position: Int) {
        adapter!!.setSelected(position)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false)
        rvNavigation = view.findViewById(R.id.rv_navigation)
        fillData()
        setAdapter()
        val isSelectorChange = getArguments()!!.getString("isSelectorChange")
        adapter!!.setSelected(Integer.parseInt(isSelectorChange!!))
        return view

    }

    override fun onItemClick(position: Int) {



        Log.e(TAG, "View$position")
        replaceFragment(position)
    }

    override fun onResume() {
        super.onResume()
        adapter!!.notifyDataSetChanged()
    }
}

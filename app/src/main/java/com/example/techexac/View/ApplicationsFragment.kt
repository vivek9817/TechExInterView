package com.example.techexac.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SwitchCompat
import androidx.compose.ui.text.capitalize
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import app.bandhan.bhp.presenter.viewholder.GenericViewHolder
import com.example.techexac.Adapter.CommonAlertAdapter
import com.example.techexac.CommonUtils.Resource
import com.example.techexac.CommonUtils.TEApplication
import com.example.techexac.CommonUtils.Utlis.checkTypeCast
import com.example.techexac.CommonUtils.Utlis.filterListByValue
import com.example.techexac.CommonUtils.Utlis.initializeRecyclerView
import com.example.techexac.CommonUtils.Utlis.setImage
import com.example.techexac.Model.AppList
import com.example.techexac.Network.AppRepository
import com.example.techexac.R
import com.example.techexac.ViewModel.ApplicationsViewModel
import com.example.techexac.ViewModel.ViewModelProviderFactory
import com.example.techexac.databinding.FragmentApplicationsBinding

class ApplicationsFragment : Fragment(R.layout.fragment_applications) {

    lateinit var binding: FragmentApplicationsBinding
    private lateinit var applicationViewModel: ApplicationsViewModel
    lateinit var applicationRecView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentApplicationsBinding.inflate(layoutInflater)
        applicationRecView = initializeRecyclerView(
            binding.recApplicationField,
            1,
            isReverseLayout = false,
            isItemDecoration = false,
            dividerDecoration = 0,
            ctx = requireContext()
        )

        AppRepository().let {
            ViewModelProviderFactory(
                requireActivity().application as TEApplication, it, requireContext()
            ).let { factory ->
                applicationViewModel =
                    ViewModelProvider(this, factory)[ApplicationsViewModel::class.java]
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onClickOnUi()
    }

    private fun onClickOnUi() {
        applicationViewModel.setRequestToTheServer()
        applicationViewModel.getresponse.observe(requireActivity(), Observer {
            it?.getContentIfNotHandled()?.let { it ->
                when (it) {
                    is Resource.Loading -> {}

                    is Resource.Success -> {
                        if (it.data?.data?.app_list?.size != 0) {
                            /*Search View*/
                            applicationRecView.adapter =
                                setDataToRecyclerView(it.data?.data?.app_list as ArrayList<Any>)
                            filterListByValue(
                                binding.fieldSearch,
                                "1",
                                it.data?.data?.app_list!!,
                                applicationRecView.adapter as CommonAlertAdapter<Any>
                            ) { arr ->
                                applicationRecView.adapter =
                                    setDataToRecyclerView(arr as ArrayList<Any>)
                            }
                        }
                    }

                    is Resource.Error -> {}
                }
            }
        })
    }

    private fun setDataToRecyclerView(anies: ArrayList<Any>): CommonAlertAdapter<Any>? {
        return object : CommonAlertAdapter<Any>(
            R.layout.layout_for_applications_list, anies
        ) {
            override fun bindData(holder: GenericViewHolder<Any>, item: Any) {
                checkTypeCast<AppList>(item).apply {
                    /*aPP Image*/
                    setImage(
                        this?.app_icon.toString(),
                        holder.itemView.findViewById<AppCompatImageView>(R.id.img),
                        requireActivity()
                    )

                    /*APP Name*/
                    holder.itemView.findViewById<AppCompatTextView>(R.id.txtApplicationName).text =
                        "${this?.app_name?.capitalize()}"

                    /*Switch*/
                    holder.itemView.findViewById<SwitchCompat>(R.id.switchBtn)
                        .setOnCheckedChangeListener { _, isChecked ->
                            if (isChecked) {
                            } else {
                            }
                        }

                    holder.itemView.findViewById<SwitchCompat>(R.id.switchBtn).isChecked = this?.notification!!

                    holder.itemView.findViewById<SwitchCompat>(R.id.switchBtn).setOnCheckedChangeListener { _, isChecked ->
                        val position = holder.adapterPosition
                        if (position == RecyclerView.NO_POSITION) {
                            return@setOnCheckedChangeListener
                        }
                       this.notification = isChecked
                    }

                }
            }

            override fun onViewRecycled(holder: GenericViewHolder<Any>) {
                holder.itemView.findViewById<SwitchCompat>(R.id.switchBtn)
                    .setOnCheckedChangeListener(null)
                super.onViewRecycled(holder)
            }

            override fun clickHanlder(pos: Int, item: Any, aView: View) {}

        }
    }


}
package com.manjil.tvapplication.guidedStep

import android.os.Bundle
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.GuidedStepSupportFragment
import androidx.leanback.widget.GuidanceStylist
import androidx.leanback.widget.GuidedAction
import com.manjil.tvapplication.R

class GuidedStepActivity : FragmentActivity() {
    companion object {
        const val TAG = "GuidedStepActivity"
        const val ACTION_CONTINUE = 0L
        const val ACTION_BACK = 1L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null)
            GuidedStepSupportFragment.add(supportFragmentManager, FirstStepFragment())
    }

    class FirstStepFragment : GuidedStepSupportFragment() {
        override fun onCreateGuidance(savedInstanceState: Bundle?): GuidanceStylist.Guidance {
            val title = "Title"
            val breadcrumb = "Breadcrumb"
            val description = "Description"
            val icon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_guided_step)

            return GuidanceStylist.Guidance(title, description, breadcrumb, icon)
        }

        override fun onCreateActions(
            actions: MutableList<GuidedAction>,
            savedInstanceState: Bundle?,
        ) {
            addAction(actions, ACTION_CONTINUE, "Continue", "Go to SecondStepFragment")
            addAction(actions, ACTION_BACK, "Cancel", "Go Back")
        }

        override fun onGuidedActionClicked(action: GuidedAction?) {
            when (action?.id) {
                ACTION_CONTINUE -> {
//                    GuidedStepSupportFragment.add(childFragmentManager,SecondStepFragment())
                }

                ACTION_BACK -> requireActivity().finish()
                else -> Log.d(TAG, "onGuidedActionClicked: Action was not defined")
            }
        }

        private fun addAction(
            actions: MutableList<GuidedAction>,
            id: Long,
            title: String,
            desc: String,
        ) {
            actions.add(
                GuidedAction.Builder(requireContext())
                    .id(id)
                    .title(title)
                    .description(desc)
                    .build()
            )
        }
    }
}
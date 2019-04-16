package silviupal.hashtagscounter.base

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.mindorks.editdrawabletext.DrawablePosition
import com.mindorks.editdrawabletext.onDrawableClickListener
import kotlinx.android.synthetic.main.fragment_base_post_actions_layout.*
import kotlinx.android.synthetic.main.merge_hashtags_counter_layout.*
import silviupal.hashtagscounter.R
import silviupal.hashtagscounter.activities.CreateOrEditItemActivity
import silviupal.hashtagscounter.helpers.SimplifiedTextWatcher

/**
 * Created by Silviu Pal on 4/10/2019.
 */
abstract class BasePostActionsFragment : BaseHashtagsCounterFragment() {
    abstract fun setActionButton()

    override fun getLayoutId(): Int = R.layout.fragment_base_post_actions_layout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActionButton()
        titleInputView.addTextChangedListener(object : SimplifiedTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                s?.let {
                    if (it.toString().isEmpty()) {
                        titleInputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    } else {
                        titleInputView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_delete, 0)
                    }
                }
                errorTitleInputView.visibility = View.GONE
            }
        })

        titleInputView.setDrawableClickListener(object : onDrawableClickListener {
            override fun onClick(target: DrawablePosition) {
                if (target == DrawablePosition.RIGHT) {
                    titleInputView.setText("")
                }
            }
        })
    }

    fun showErrors(titleText: String, postText: String) {
        if (titleText.isEmpty()) {
            errorTitleInputView.visibility = View.VISIBLE
        }
        if (postText.isEmpty()) {
            tvInputError.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as CreateOrEditItemActivity
    }

    override fun onDetach() {
        super.onDetach()
        job.cancel()
    }
}

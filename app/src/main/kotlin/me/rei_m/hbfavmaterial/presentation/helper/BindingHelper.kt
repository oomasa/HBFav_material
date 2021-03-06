/*
 * Copyright (c) 2017. Rei Matsushita
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package me.rei_m.hbfavmaterial.presentation.helper

import android.databinding.BindingAdapter
import android.support.annotation.IdRes
import android.support.design.widget.NavigationView
import android.support.design.widget.TextInputLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.SwipeRefreshLayout
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import me.rei_m.hbfavmaterial.R
import me.rei_m.hbfavmaterial.presentation.util.BookmarkUtil
import me.rei_m.hbfavmaterial.presentation.widget.graphics.RoundedTransformation

class BindingHelper {
    companion object {

        @JvmStatic
        @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String) {

            if (url.isEmpty()) return

            Picasso.with(view.context)
                    .load(url)
                    .into(view)
        }

        @JvmStatic
        @BindingAdapter("imageUrl", "isRound")
        fun loadImage(view: ImageView, url: String, isRound: Boolean) {

            if (url.isEmpty()) return

            if (isRound) {
                Picasso.with(view.context)
                        .load(url)
                        .transform(RoundedTransformation())
                        .into(view)
            } else {
                loadImage(view, url)
            }
        }

        @JvmStatic
        @BindingAdapter("onRefresh")
        fun onRefresh(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener) {
            view.setOnRefreshListener(listener)
        }

        @JvmStatic
        @BindingAdapter("colorSchemeResource1", "colorSchemeResource2", "colorSchemeResource3")
        fun colorSchemeResources(view: SwipeRefreshLayout, colorRes1: Int, colorRes2: Int, colorRes3: Int) {
            view.setColorSchemeColors(colorRes1, colorRes2, colorRes3)
        }

        @JvmStatic
        @BindingAdapter("userId")
        fun userId(view: NavigationView, userId: String) {

            val headerView = view.getHeaderView(0)
            val imageOwnerIcon = headerView.findViewById<ImageView>(R.id.nav_header_main_image_owner_icon)

            Picasso.with(view.context)
                    .load(BookmarkUtil.getLargeIconImageUrlFromId(userId))
                    .resizeDimen(R.dimen.icon_size_nav_crop, R.dimen.icon_size_nav_crop).centerCrop()
                    .transform(RoundedTransformation())
                    .into(imageOwnerIcon)

            val textOwnerId = headerView.findViewById<TextView>(R.id.nav_header_main_text_owner_name)
            textOwnerId.text = userId
        }

        @JvmStatic
        @BindingAdapter("checkedNavId")
        fun checkedNavId(view: NavigationView, @IdRes checkedNavId: Int) {
            view.setCheckedItem(checkedNavId)
        }

        @JvmStatic
        @BindingAdapter("currentItem")
        fun currentItem(view: ViewPager, currentItem: Int) {
            view.currentItem = currentItem
        }

        @JvmStatic
        @BindingAdapter("errorText")
        fun errorText(view: TextInputLayout, errorText: String) {
            view.error = errorText
        }
    }
}

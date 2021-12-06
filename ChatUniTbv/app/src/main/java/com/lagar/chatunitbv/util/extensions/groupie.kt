package com.lagar.chatunitbv.util.extensions

import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.Section

fun GroupieAdapter.update(section: Section) {
    val sections = ArrayList<Section>()
    sections.add(section)
    this.update(sections)
}
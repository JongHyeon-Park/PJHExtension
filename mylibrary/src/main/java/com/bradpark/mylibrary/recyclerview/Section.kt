package com.siwonschool.ui.recyclerview

open class Section(val firstPosition: Int,
                   var title: String,
                   val originalTitle: String,
                   var sortTitle: String = "",
                   var sectionedPosition: Int = 0,
                   var sortPosition: Int = 0,
                   var curriculumImage: String ="",         //커리큘럼 image url 주소
                   var seeCurriculum: String ="")           //커리큘럼 팝업 호출 시 기본 타이틀 필요
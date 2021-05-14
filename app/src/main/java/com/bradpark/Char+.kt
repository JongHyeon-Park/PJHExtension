package com.bradpark

// 초성목록
val KOREAN_INITIAL_LIST: List<Char> = listOf('ㄱ','ㄲ','ㄴ','ㄷ','ㄸ','ㄹ','ㅁ','ㅂ','ㅃ','ㅅ','ㅆ','ㅇ','ㅈ','ㅉ','ㅊ','ㅋ','ㅌ','ㅍ','ㅎ')

// 중성목록
val KOREAN_NEUTRALITY_LIST: List<Char> = listOf('ㅏ','ㅐ','ㅑ','ㅒ','ㅓ','ㅔ','ㅕ','ㅖ','ㅗ','ㅘ','ㅙ','ㅚ','ㅛ','ㅜ','ㅝ','ㅞ','ㅟ','ㅠ','ㅡ','ㅢ','ㅣ')

// 받침목록
val KOREAN_SUPPORT_LIST: List<Char?> = listOf(null, 'ㄱ','ㄲ','ㄳ','ㄴ','ㄵ','ㄶ','ㄷ','ㄹ','ㄺ','ㄻ','ㄼ','ㄽ','ㄾ','ㄿ','ㅀ','ㅁ','ㅂ','ㅄ','ㅅ','ㅆ','ㅇ','ㅈ', 'ㅊ','ㅋ','ㅌ','ㅍ','ㅎ')

// 한글 유니코드 처음
val KOREAN_FIRST: Char = '가'

// 한글 유니코드 마지막
val KOREAN_LAST: Char = '힣'

// 초성 시작
val KOREAN_FIRST_CONSONANT = 'ㄱ'

// 초성 마지막
val KOREAN_LAST_CONSONANT = 'ㅎ'

// 중성 처음
val KOREAN_FIRST_VOWEL = 'ㅏ'

// 중성 마지막
val KOREAN_LAST_VOWEL = 'ㅣ'

// 한글 범위검사
val Char.isKorean: Boolean
    // 한글 낱말인가?
    get() = isKoreanWord ||
            // 자음인가?
            isConsonant ||
            // 모음인가?
            isVowel

// 한글 낱말인가?
val Char.isKoreanWord: Boolean
    get() = this in KOREAN_FIRST..KOREAN_LAST

// 자음인가?
val Char.isConsonant: Boolean
    get() = this in KOREAN_FIRST_CONSONANT..KOREAN_LAST_CONSONANT

// 모음인가?
val Char.isVowel: Boolean
    get() = this in KOREAN_FIRST_VOWEL..KOREAN_LAST_VOWEL

// 한글 글자인가?
val Char.initialOf: Int
    get() = minus(KOREAN_FIRST)
        .div(KOREAN_SUPPORT_LIST.size)
        .div(KOREAN_NEUTRALITY_LIST.size)

// 중성 인덱스를 가져옴
val Char.neutralityOf: Int
    get() = minus(KOREAN_FIRST)
        .div(KOREAN_SUPPORT_LIST.size)
        .rem(KOREAN_NEUTRALITY_LIST.size)

// 받침 인덱스를 가져옴
val Char.supportOf: Int
    get() = minus(KOREAN_FIRST)
        .rem(KOREAN_SUPPORT_LIST.size)

// 한글을 자음 모음 받침으로 분리
fun Char.separationKorean(): Triple<Char?, Char?, Char?> {
    return when {
        isKoreanWord -> Triple(KOREAN_INITIAL_LIST.getOrNull(initialOf),
            KOREAN_NEUTRALITY_LIST.getOrNull(neutralityOf),
            KOREAN_SUPPORT_LIST.getOrNull(supportOf))
        isConsonant -> Triple(this, null, null)
        isVowel -> Triple(null, this, null)
        else -> Triple(null, null, null)
    }
}
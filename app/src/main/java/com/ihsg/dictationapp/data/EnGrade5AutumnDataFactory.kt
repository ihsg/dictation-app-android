package com.ihsg.dictationapp.data

import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.LessonRepository
import com.ihsg.dictationapp.model.repository.WordRepository
import javax.inject.Inject

class EnGrade5AutumnDataFactory @Inject constructor(
    private val lessonRepository: LessonRepository,
    private val wordRepository: WordRepository,
) : DataFactory, BookSettings, GradeSettings {
    private val logger = Logger(this)

    private var bookId = 0L
    private var gradeId = 0L

    override fun setBookId(id: Long) {
        bookId = id
    }

    override fun setGradeId(id: Long) {
        gradeId = id
    }

    override suspend fun buildData() {
        logger.d { "buildData called: bookId = $bookId, gradeId = $gradeId" }

        mapOf(
            buildUnit1(),
            buildUnit2(),
            buildUnit3(),
            buildUnit4(),
            buildUnit5(),
            buildUnit6(),
            buildUnit7(),
            buildUnit8(),
        ).forEach { (lessonName, words) ->
            lessonRepository.add(
                LessonEntity(
                    bookId = bookId,
                    gradeId = gradeId,
                    name = lessonName
                )
            )

            val lessonId = lessonRepository
                .loadAll(bookId = bookId, gradeId = gradeId)
                ?.firstOrNull { it.name == lessonName }?.id

            if (lessonId == null) {
                return@forEach
            }

            words.forEach { word ->
                wordRepository.add(
                    WordEntity(
                        bookId = bookId,
                        gradeId = gradeId,
                        lessonId = lessonId,
                        word = word.first,
                        tips = word.second,
                        strokeCount = word.first.length
                    )
                )
            }
        }



        logger.d { "buildData finished" }
    }

    private fun buildUnit1() = "Unit 1 Goldilocks and the three bears" to listOf(
        "bear" to "熊",
        "forest" to "森林",
        "there" to "（与be连用）有",
        "house" to "房子",
        "soup" to "汤",
        "just right" to "正合适，正好",
        "room" to "房间",
        "hard" to "硬的",
        "soft" to "柔软的",
        "afraid" to "害怕",
        "in front of" to "在……前面",
        "her" to "她",
        "Help!" to "救命啊！",
        "beside" to "在……旁边",
        "between" to "在……中间",
        "China" to "中国",
        "really" to "真的",
        "then" to "然后",
        "find" to "找到，发现",
        "their" to "他们的；她们的；它们的",
    )

    private fun buildUnit2() = "Unit 2 A new student" to listOf(
        "student" to "学生",
        "show…around" to "带……参观",
        "classroom" to "教室",
        "second" to "第二",
        "floor" to "楼层",
        "computer" to "电脑",
        "third" to "第三",
        "first" to "第一；首先",
        "swing" to "秋千",
        "push" to "推",
        "heavy" to "重的，沉的",
        "stop" to "停下，停止",
        "high" to "高的",
        "great" to "很多的，极大的",
    )

    private fun buildUnit3() = "Unit 3 Our animal school" to listOf(
        "one…, the other…" to "一个……，另一个……",
        "body" to "身体",
        "no" to "没有，无",
        "leg" to "腿",
        "or" to "也不，也没有",
        "arm" to "手臂",
        "wing" to "翅膀",
        "foot" to "脚，足",
        "rabbit" to "兔子",
        "give" to "给",
        "finger" to "手指",
    )

    private fun buildUnit4() = "Unit 4 Hobbies" to listOf(
        "hobby" to "业余爱好",
        "be good at" to "擅长于",
        "with" to "与……一起",
        "also" to "也",
        "read" to "读；阅读",
        "story" to "故事",
        "a lot of" to "很多",
        "play the piano" to "弹钢琴",
        "dance" to "跳舞",
        "watch films" to "看电影",
        "both" to "两个都",
        "sing" to "唱歌",
        "group" to "组",
        "about" to "关于",
        "idea" to "主意",
        "ice" to "冰",
        "hole" to "洞",
        "Look out!" to "当心！注意！",
        "wet" to "湿的，潮的",
    )

    private fun buildUnit5() = "Unit 5 What do they do?" to listOf(
        "teacher" to "老师",
        "teach" to "教",
        "writer" to "作家",
        "write" to "写",
        "work" to "工作",
        "at home" to "在家",
        "doctor" to "医生",
        "help" to "帮助",
        "sick" to "生病的",
        "people" to "人；人们",
        "factory" to "工厂",
        "worker" to "工人",
        "cook" to "厨师",
        "driver" to "驾驶员，司机",
        "farmer" to "农民",
        "nurse" to "护士",
        "policeman" to "警察",
    )

    private fun buildUnit6() = "Unit 6 My e-friend" to listOf(
        "e-friend" to "网友",
        "Wait a minute" to "等一会儿",
        "send" to "发出（信件，邮件等）",
        "email" to "电子邮件",
        "to" to "给",
        "live" to "住，居住",
        "UK" to "英国",
        "… years old" to "……岁",
        "study" to "学习",
        "Canada" to "加拿大",
        "Greece" to "希腊",
        "Russia" to "俄罗斯",
        "PRC" to "中华人民共和国",
        "US" to "美国",
        "go fishing" to "去钓鱼",
        "tomorrow" to "明天",
        "fishing" to "钓鱼，捕鱼",
        "Don't worry" to "别担心",
        "sit" to "坐，坐在",
        "by…" to "在……旁边",
        "wait" to "等，等待",
    )

    private fun buildUnit7() = "Unit 7 At weekends" to listOf(
        "at weekends" to "在周末",
        "visit" to "拜访；参观",
        "grandparent" to "祖父；祖母；外祖父；外祖母",
        "play with" to "和……一起玩",
        "very much" to "非常",
        "often" to "经常，常常",
        "chat" to "聊天",
        "Internet" to "网络，互联网",
        "always" to "总是，一直",
        "sometimes" to "有时",
        "go to the cinema" to "去看电影",
        "there" to "那里",
        "a lot" to "很多",
        "come out" to "出来",
        "go out" to "出来",
    )

    private fun buildUnit8() = "Unit 8 At Christmas" to listOf(
        "Christmas" to "圣诞节",
        "buy" to "买",
        "present" to "礼物",
        "Christmas tree" to "圣诞树",
        "Father Christmas" to "圣诞老人",
        "next" to "接着，然后",
        "put" to "放",
        "pretty" to "漂亮的，好看的",
        "thing" to "物品，东西",
        "look" to "看起来",
        "Christmas Eve" to "圣诞夜，平安夜",
        "stocking" to "长筒袜",
        "wait for" to "等候，等待",
        "finally" to "最后",
        "Christmas Day" to "圣诞节",
        "early" to "早早地",
        "turkey" to "火鸡；火鸡肉",
        "pudding" to "布丁",
        "all" to "全都",
        "have a good time" to "过得愉快，玩得高兴",
        "card" to "卡片",
        "children" to "孩子，儿童",
        "message" to "信息，消息",
        "Merry Christmas!" to "圣诞快乐！",
        "song" to "歌曲",
        "What's wrong with…?" to "……怎么了？",
        "him" to "他",
        "us" to "我们",
        "letter" to "信",
        "storybook" to "故事书",
        "after" to "在……以后",
    )
}
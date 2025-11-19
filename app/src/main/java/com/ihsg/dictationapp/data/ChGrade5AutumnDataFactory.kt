package com.ihsg.dictationapp.data

import com.ihsg.dictationapp.model.db.entity.LessonEntity
import com.ihsg.dictationapp.model.db.entity.WordEntity
import com.ihsg.dictationapp.model.log.Logger
import com.ihsg.dictationapp.model.repository.LessonRepository
import com.ihsg.dictationapp.model.repository.WordRepository
import com.ihsg.dictationapp.model.util.ChineseUtil.getHanziStrokeCount
import com.ihsg.dictationapp.model.util.ChineseUtil.hanziToPinyin
import javax.inject.Inject

class ChGrade5AutumnDataFactory @Inject constructor(
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
            buildLesson1(),
            buildLesson2(),
            buildLesson3(),
            buildLesson5(),
            buildLesson6(),
            buildLesson7(),
            buildLesson8(),
            buildLesson9(),
            buildLesson10(),
            buildLesson14(),
            buildLesson16(),
            buildLesson17(),
            buildLesson18(),
            buildLesson19(),
            buildLesson22(),
            buildLesson25(),
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
                        word = word,
                        tips = hanziToPinyin(word),
                        count = getHanziStrokeCount(word)
                    )
                )
            }
        }



        logger.d { "buildData finished" }
    }

    private fun buildLesson1() = "第1课 白鹭" to listOf(
        "精巧",
        "配合",
        "身段",
        "适宜",
        "白鹤",
        "生硬",
        "寻常",
        "忘却",
        "镜匣",
        "孤独",
        "悠然",
        "嗜好",
        "黄昏",
        "恩惠",
        "美中不足"
    )

    private fun buildLesson2() = "第2课 落花生" to listOf(
        "播种",
        "浇水",
        "吩咐",
        "榨油",
        "爱慕",
        "体面"
    )

    private fun buildLesson3() = "第3课 桂花雨" to listOf(
        "桂花",
        "懂得",
        "糕饼",
        "茶叶",
    )

    private fun buildLesson5() = "第5课 搭石" to listOf(
        "汛期",
        "山洪",
        "暴发",
        "间隔",
        "唯独",
        "懒惰",
        "平稳",
        "难免",
        "保持",
        "平衡",
        "协调",
        "美感",
        "示意",
        "家常",
        "假如",
        "理所当然",
        "联结"
    )

    private fun buildLesson6() = "第6课 将相和" to listOf(
        "无价之宝",
        "召集",
        "大臣",
        "商议",
        "解决",
        "完好无缺",
        "称赞",
        "商量",
        "允诺",
        "典礼",
        "承诺",
        "得罪",
        "胆怯",
        "示弱",
        "拒绝",
        "职位",
        "同心协力"
    )

    private fun buildLesson7() = "第7课 什么比猎豹的速度更快" to listOf(
        "猎豹",
        "冠军",
        "陆地",
        "俯冲",
        "高速公路",
        "搭乘",
        "火箭",
        "发动机",
        "手电筒",
        "赤道",
        "难以置信"
    )

    private fun buildLesson8() = "第8课 冀中地道战" to listOf(
        "侵略",
        "修筑",
        "粉碎",
        "领导",
        "不计其数",
        "打击",
        "坚持",
        "游击",
        "隐藏",
        "陷坑",
        "拐弯",
        "无穷无尽"
    )

    private fun buildLesson9() = "第9课 猎人海力布" to listOf(
        "猎物",
        "酬谢",
        "珍宝",
        "叮嘱",
        "复活",
        "议论",
        "崩塌",
        "焦急",
        "发誓",
        "千真万确",
        "谎话",
        "迟延",
        "灾难",
        "镇定",
        "后悔",
        "悲痛",
        "震天动地"
    )

    private fun buildLesson10() = "第10课 牛郎织女（一）" to listOf(
        "嫂子",
        "剩饭",
        "床铺",
        "亲密",
        "笑嘻嘻",
        "成家立业",
        "好歹",
        "稀罕",
        "妻子",
        "晚霞",
        "一辈子",
        "结婚",
        "相依为命"
    )

    private fun buildLesson14() = "第14课 圆明园的毁灭" to listOf(
        "毁灭",
        "不可估量",
        "损失",
        "举世闻名",
        "众星捧月",
        "金碧辉煌",
        "殿堂",
        "象征",
        "仿照",
        "诗情画意",
        "建筑",
        "漫游",
        "天南海北",
        "饱览",
        "风景名胜",
        "境界",
        "宏伟",
        "奇珍异宝",
        "博物馆",
        "搬运",
        "毁灭",
        "罪证",
        "奉命"
    )

    private fun buildLesson16() = "第16课 太阳" to listOf(
        "寸草不生",
        "摄氏度",
        "繁殖",
        "粮食",
        "煤炭",
        "漂浮",
        "地区",
        "杀菌",
        "治疗"
    )

    private fun buildLesson17() = "第17课 松鼠" to listOf(
        "松鼠",
        "乖巧",
        "清秀",
        "玲珑",
        "尾巴",
        "歇凉",
        "追逐",
        "警觉",
        "触动",
        "光滑",
        "狭窄",
        "勉强",
        "脱落",
        "梳理"
    )

    private fun buildLesson18() = "第18课 慈母情深" to listOf(
        "长篇",
        "连续",
        "广播",
        "铁路",
        "辞退",
        "挣钱",
        "压抑",
        "潮湿",
        "忙碌",
        "阴暗",
        "酷暑",
        "炎夏",
        "噪声",
        "瘦弱",
        "脊背",
        "口罩",
        "忍心",
        "机械",
        "数落",
        "权利"
    )

    private fun buildLesson19() = "第19课 父爱之舟" to listOf(
        "渔船",
        "报考",
        "教训",
        "心疼",
        "席子",
        "庙会",
        "彩排",
        "糖果",
        "抽象",
        "启迪",
        "毕业",
        "寄宿",
        "师范",
        "路费",
        "轮换",
        "领略",
        "意境",
        "磨灭",
        "精致"
    )

    private fun buildLesson22() = "第22课 鸟的天堂" to listOf(
        "陆续",
        "白茫茫",
        "榕树",
        "纠正",
        "不可计数",
        "照耀",
        "涨潮",
        "树梢",
        "应接不暇",
        "画眉"
    )

    private fun buildLesson25() = "第25课 忆读书" to listOf(
        "舅父",
        "津津有味",
        "英雄",
        "无限",
        "一知半解",
        "述说",
        "厌烦",
        "荒唐",
        "辛酸",
        "访问",
        "书刊",
        "烦琐",
        "真情实感",
        "质朴",
        "刊物"
    )
}
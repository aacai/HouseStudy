package zhiqiu.hs.i18n

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zhiqiu.hs.core.Element
import zhiqiu.hs.core.FortuneStar
import zhiqiu.hs.core.Gender
import zhiqiu.hs.core.Group
import zhiqiu.hs.generated.resources.*

@Composable
fun genderLabel(gender: Gender): String = stringResource(
    when (gender) {
        Gender.男 -> Res.string.gender_male
        Gender.女 -> Res.string.gender_female
    }
)

@Composable
fun groupLabel(group: Group): String = stringResource(
    when (group) {
        Group.东四 -> Res.string.group_east
        Group.西四 -> Res.string.group_west
    }
)

@Composable
fun elementLabel(element: Element): String = stringResource(
    when (element) {
        Element.金 -> Res.string.element_metal
        Element.木 -> Res.string.element_wood
        Element.水 -> Res.string.element_water
        Element.火 -> Res.string.element_fire
        Element.土 -> Res.string.element_earth
    }
)

@Composable
fun starLabel(star: FortuneStar): String = stringResource(
    when (star) {
        FortuneStar.伏位 -> Res.string.star_fuwei
        FortuneStar.生气 -> Res.string.star_shengqi
        FortuneStar.延年 -> Res.string.star_yannian
        FortuneStar.天医 -> Res.string.star_tianyi
        FortuneStar.绝命 -> Res.string.star_jueming
        FortuneStar.五鬼 -> Res.string.star_wugui
        FortuneStar.六煞 -> Res.string.star_liusha
        FortuneStar.祸害 -> Res.string.star_huohai
    }
)

@Composable
fun trigramLabel(key: String): String {
    val res = when (key) {
        "乾" -> Res.string.gua_qian
        "坤" -> Res.string.gua_kun
        "震" -> Res.string.gua_zhen
        "巽" -> Res.string.gua_xun
        "坎" -> Res.string.gua_kan
        "离" -> Res.string.gua_li
        "艮" -> Res.string.gua_gen
        "兑" -> Res.string.gua_dui
        else -> null
    } ?: return key
    return stringResource(res)
}

@Composable
fun directionLabel(key: String): String {
    val res = when (key) {
        "西北" -> Res.string.dir_nw
        "西" -> Res.string.dir_w
        "南" -> Res.string.dir_s
        "东" -> Res.string.dir_e
        "东南" -> Res.string.dir_se
        "北" -> Res.string.dir_n
        "东北" -> Res.string.dir_ne
        "西南" -> Res.string.dir_sw
        "中宫" -> Res.string.dir_center
        else -> null
    } ?: return key
    return stringResource(res)
}

@Composable
fun memberLabel(name: String): String {
    val res = when (name) {
        "父", "父亲" -> Res.string.member_father
        "母", "母亲" -> Res.string.member_mother
        "长子" -> Res.string.member_eldest_son
        "二子", "次子" -> Res.string.member_second_son
        "三子" -> Res.string.member_third_son
        "长女" -> Res.string.member_eldest_daughter
        "二女", "次女" -> Res.string.member_second_daughter
        "三女" -> Res.string.member_third_daughter
        "厕", "厕所" -> Res.string.member_toilet
        "厨", "厨房" -> Res.string.member_kitchen
        "厅", "客厅" -> Res.string.member_living
        else -> null
    } ?: return name
    return stringResource(res)
}

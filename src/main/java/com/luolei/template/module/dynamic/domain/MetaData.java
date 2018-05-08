package com.luolei.template.module.dynamic.domain;

import com.luolei.template.domain.AbstractAuditingEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 罗雷
 * @date 2018/3/15 0015
 * @time 9:29
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "t_meta_data")
public class MetaData extends AbstractAuditingEntity {

    /**
     * 租户
     */
    private MetaTenant tenant;

    /**
     * 对象
     */
    private MetaObject object;

    /**
     * 数据名
     */
    private String name;

    public String getValue(int num) {
        switch(num) {
            case 0 : return getValue0();
            case 1 : return getValue1();
            case 2 : return getValue2();
            case 3 : return getValue3();
            case 4 : return getValue4();
            case 5 : return getValue5();
            case 6 : return getValue6();
            case 7 : return getValue7();
            case 8 : return getValue8();
            case 9 : return getValue9();
            case 10 : return getValue10();
            case 11 : return getValue11();
            case 12 : return getValue12();
            case 13 : return getValue13();
            case 14 : return getValue14();
            case 15 : return getValue15();
            case 16 : return getValue16();
            case 17 : return getValue17();
            case 18 : return getValue18();
            case 19 : return getValue19();
            case 20 : return getValue20();
            case 21 : return getValue21();
            case 22 : return getValue22();
            case 23 : return getValue23();
            case 24 : return getValue24();
            case 25 : return getValue25();
            case 26 : return getValue26();
            case 27 : return getValue27();
            case 28 : return getValue28();
            case 29 : return getValue29();
            case 30 : return getValue30();
            case 31 : return getValue31();
            case 32 : return getValue32();
            case 33 : return getValue33();
            case 34 : return getValue34();
            case 35 : return getValue35();
            case 36 : return getValue36();
            case 37 : return getValue37();
            case 38 : return getValue38();
            case 39 : return getValue39();
            case 40 : return getValue40();
            case 41 : return getValue41();
            case 42 : return getValue42();
            case 43 : return getValue43();
            case 44 : return getValue44();
            case 45 : return getValue45();
            case 46 : return getValue46();
            case 47 : return getValue47();
            case 48 : return getValue48();
            case 49 : return getValue49();
            case 50 : return getValue50();
            case 51 : return getValue51();
            case 52 : return getValue52();
            case 53 : return getValue53();
            case 54 : return getValue54();
            case 55 : return getValue55();
            case 56 : return getValue56();
            case 57 : return getValue57();
            case 58 : return getValue58();
            case 59 : return getValue59();
            case 60 : return getValue60();
            case 61 : return getValue61();
            case 62 : return getValue62();
            case 63 : return getValue63();
            case 64 : return getValue64();
            case 65 : return getValue65();
            case 66 : return getValue66();
            case 67 : return getValue67();
            case 68 : return getValue68();
            case 69 : return getValue69();
            case 70 : return getValue70();
            case 71 : return getValue71();
            case 72 : return getValue72();
            case 73 : return getValue73();
            case 74 : return getValue74();
            case 75 : return getValue75();
            case 76 : return getValue76();
            case 77 : return getValue77();
            case 78 : return getValue78();
            case 79 : return getValue79();
            case 80 : return getValue80();
            case 81 : return getValue81();
            case 82 : return getValue82();
            case 83 : return getValue83();
            case 84 : return getValue84();
            case 85 : return getValue85();
            case 86 : return getValue86();
            case 87 : return getValue87();
            case 88 : return getValue88();
            case 89 : return getValue89();
            case 90 : return getValue90();
            case 91 : return getValue91();
            case 92 : return getValue92();
            case 93 : return getValue93();
            case 94 : return getValue94();
            case 95 : return getValue95();
            case 96 : return getValue96();
            case 97 : return getValue97();
            case 98 : return getValue98();
            case 99 : return getValue99();
            case 100 : return getValue100();
            default: throw new IllegalArgumentException("invalid index num:" + num);
        }
    }

    public void setValue(int num, String value) {
        switch(num) {
            case 0 : setValue0(value);break;
            case 1 : setValue1(value);break;
            case 2 : setValue2(value);break;
            case 3 : setValue3(value);break;
            case 4 : setValue4(value);break;
            case 5 : setValue5(value);break;
            case 6 : setValue6(value);break;
            case 7 : setValue7(value);break;
            case 8 : setValue8(value);break;
            case 9 : setValue9(value);break;
            case 10 : setValue10(value);break;
            case 11 : setValue11(value);break;
            case 12 : setValue12(value);break;
            case 13 : setValue13(value);break;
            case 14 : setValue14(value);break;
            case 15 : setValue15(value);break;
            case 16 : setValue16(value);break;
            case 17 : setValue17(value);break;
            case 18 : setValue18(value);break;
            case 19 : setValue19(value);break;
            case 20 : setValue20(value);break;
            case 21 : setValue21(value);break;
            case 22 : setValue22(value);break;
            case 23 : setValue23(value);break;
            case 24 : setValue24(value);break;
            case 25 : setValue25(value);break;
            case 26 : setValue26(value);break;
            case 27 : setValue27(value);break;
            case 28 : setValue28(value);break;
            case 29 : setValue29(value);break;
            case 30 : setValue30(value);break;
            case 31 : setValue31(value);break;
            case 32 : setValue32(value);break;
            case 33 : setValue33(value);break;
            case 34 : setValue34(value);break;
            case 35 : setValue35(value);break;
            case 36 : setValue36(value);break;
            case 37 : setValue37(value);break;
            case 38 : setValue38(value);break;
            case 39 : setValue39(value);break;
            case 40 : setValue40(value);break;
            case 41 : setValue41(value);break;
            case 42 : setValue42(value);break;
            case 43 : setValue43(value);break;
            case 44 : setValue44(value);break;
            case 45 : setValue45(value);break;
            case 46 : setValue46(value);break;
            case 47 : setValue47(value);break;
            case 48 : setValue48(value);break;
            case 49 : setValue49(value);break;
            case 50 : setValue50(value);break;
            case 51 : setValue51(value);break;
            case 52 : setValue52(value);break;
            case 53 : setValue53(value);break;
            case 54 : setValue54(value);break;
            case 55 : setValue55(value);break;
            case 56 : setValue56(value);break;
            case 57 : setValue57(value);break;
            case 58 : setValue58(value);break;
            case 59 : setValue59(value);break;
            case 60 : setValue60(value);break;
            case 61 : setValue61(value);break;
            case 62 : setValue62(value);break;
            case 63 : setValue63(value);break;
            case 64 : setValue64(value);break;
            case 65 : setValue65(value);break;
            case 66 : setValue66(value);break;
            case 67 : setValue67(value);break;
            case 68 : setValue68(value);break;
            case 69 : setValue69(value);break;
            case 70 : setValue70(value);break;
            case 71 : setValue71(value);break;
            case 72 : setValue72(value);break;
            case 73 : setValue73(value);break;
            case 74 : setValue74(value);break;
            case 75 : setValue75(value);break;
            case 76 : setValue76(value);break;
            case 77 : setValue77(value);break;
            case 78 : setValue78(value);break;
            case 79 : setValue79(value);break;
            case 80 : setValue80(value);break;
            case 81 : setValue81(value);break;
            case 82 : setValue82(value);break;
            case 83 : setValue83(value);break;
            case 84 : setValue84(value);break;
            case 85 : setValue85(value);break;
            case 86 : setValue86(value);break;
            case 87 : setValue87(value);break;
            case 88 : setValue88(value);break;
            case 89 : setValue89(value);break;
            case 90 : setValue90(value);break;
            case 91 : setValue91(value);break;
            case 92 : setValue92(value);break;
            case 93 : setValue93(value);break;
            case 94 : setValue94(value);break;
            case 95 : setValue95(value);break;
            case 96 : setValue96(value);break;
            case 97 : setValue97(value);break;
            case 98 : setValue98(value);break;
            case 99 : setValue99(value);break;
            case 100 : setValue100(value);break;
            default: throw new IllegalArgumentException("invalid index num:" + num);
        }
    }

    // 一下是value字段
    private String value0;
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
    private String value6;
    private String value7;
    private String value8;
    private String value9;
    private String value10;
    private String value11;
    private String value12;
    private String value13;
    private String value14;
    private String value15;
    private String value16;
    private String value17;
    private String value18;
    private String value19;
    private String value20;
    private String value21;
    private String value22;
    private String value23;
    private String value24;
    private String value25;
    private String value26;
    private String value27;
    private String value28;
    private String value29;
    private String value30;
    private String value31;
    private String value32;
    private String value33;
    private String value34;
    private String value35;
    private String value36;
    private String value37;
    private String value38;
    private String value39;
    private String value40;
    private String value41;
    private String value42;
    private String value43;
    private String value44;
    private String value45;
    private String value46;
    private String value47;
    private String value48;
    private String value49;
    private String value50;
    private String value51;
    private String value52;
    private String value53;
    private String value54;
    private String value55;
    private String value56;
    private String value57;
    private String value58;
    private String value59;
    private String value60;
    private String value61;
    private String value62;
    private String value63;
    private String value64;
    private String value65;
    private String value66;
    private String value67;
    private String value68;
    private String value69;
    private String value70;
    private String value71;
    private String value72;
    private String value73;
    private String value74;
    private String value75;
    private String value76;
    private String value77;
    private String value78;
    private String value79;
    private String value80;
    private String value81;
    private String value82;
    private String value83;
    private String value84;
    private String value85;
    private String value86;
    private String value87;
    private String value88;
    private String value89;
    private String value90;
    private String value91;
    private String value92;
    private String value93;
    private String value94;
    private String value95;
    private String value96;
    private String value97;
    private String value98;
    private String value99;
    private String value100;
}

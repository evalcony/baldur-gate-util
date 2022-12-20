package com.eval.grammar;

import com.eval.Container;
import com.eval.util.SourceProccessor;

import java.util.*;

// 宏命令
/*
    在 macro 文件中定义宏：
        $LTimer(<param1>,<param2>) SetGlobalTimer(<param1>,"LOCALS",<param2>)
    在脚本代码文件中写如下代码：
        $LTimer(<a>,<b>)
    解析结果：
        SetGlobalTimer(<a>,"LOCALS",<b>)
    对参数顺序没有要求，但是需要一一对应。参数之间用,隔开，遵从一般的方法传参习惯
 */
public class MacroCmdFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "macro";
    }

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        return container.rawLines.get(startIndex).contains("$");
    }

    @Override
    boolean compile(Container container, int startIndex) {
        // 去除首尾空格、头部$符合
        String macroCmd = container.rawLines.get(startIndex).trim();
        String macroName = getMacroName(macroCmd);

        /*
         宏的元素顺序可以调换，例如 $cmd(p1,p2) aaa(p2,p1)
         宏定义 cmd(p1,p2,p3...)
         宏替换 cccmmmddd(p3,p2,p1)
         脚本编写 cmd(x,y,z)
         期望结果 cccmmmddd(z,y,x)

        宏定义 <pos:p_name> - ListP 用下标表示pos
        宏替换 [p_name] - List 不需要
        脚本编写 <pos:v> - ListV 用下标表示pos
        期望结果 [p_name->v] - replaceBlock.replace(ListP[i],ListV[i])

         */

        List<String> listV = findAllValueList(container.rawLines.get(startIndex)); // 实际替换值列表，按照定义的先后顺序读入
        String macroCmdBlock = SourceProccessor.getMacroCmdBlock().get(macroName);
        String macroReplaceBlock= SourceProccessor.getMacroReplaceBlock().get(macroName);
        if (macroCmdBlock == null) {
            System.out.println(container.rawLines.get(startIndex));
        }
        List<String> listP = findMacroParamList(macroCmdBlock); // 占位符列表
        for (int i = 0; i < listP.size(); ++i) {
            macroReplaceBlock = macroReplaceBlock.replace(listP.get(i), listV.get(i));
        }
        container.rawLines.set(startIndex, macroReplaceBlock);

        return true;
    }

    public static void main(String[] args) {

        String macroCmd = "(<v1>,<v2>,<v3>)";
        List<String> cmdList = new MacroCmdFunction().findAllValueList(macroCmd);
        System.out.println(cmdList);
    }

    public static String getMacroName(String macroCmd) {
        int lBracketPos = macroCmd.indexOf("(");
        if (lBracketPos == -1) {
            return macroCmd;
        }
        return macroCmd.substring(1, lBracketPos).trim();
    }

    private List<String> findMacroParamList(String macroCmd) {
        int lBracketPos = macroCmd.indexOf("(");
        if (lBracketPos == -1)
            return Collections.emptyList();
        int rBracketPos = macroCmd.lastIndexOf(")");

        List<String> paramList = new ArrayList<>();
        String sentence = macroCmd.substring(lBracketPos+1, rBracketPos);

        String[] params = sentence.split(",");
        for (int i = 0; i < params.length; ++i) {
            paramList.add(params[i].trim());
        }
        return paramList;
    }
    private List<String> findAllValueList(String macroCmd) {

        int lBracketPos = macroCmd.indexOf("(");
        if (lBracketPos == -1)
            return Collections.emptyList();
        int rBracketPos = macroCmd.lastIndexOf(")");

        List<String> valueList = new ArrayList<>();
        String sentence = macroCmd.substring(lBracketPos+1, rBracketPos);

        String[] values = sentence.split(",");
        for (int i = 0; i < values.length; ++i) {
            valueList.add(values[i].trim());
        }
        return valueList;
    }
}

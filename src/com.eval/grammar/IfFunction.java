package com.eval.grammar;

import com.eval.Container;
import com.eval.util.LineUtils;

import java.util.ArrayList;
import java.util.List;

public class IfFunction extends AbstractFunction {

    /*
    if (condition) {
        statement
    }
     */

    @Override
    boolean check(Container container, int startIndex) throws Exception {
        if (container == null || container.rawLines == null || container.rawLines.isEmpty() || startIndex >= container.rawLines.size()) {
            return false;
        }
        if (!container.rawLines.get(startIndex).contains("if")) {
            return false;
        }
        return true;
    }

    @Override
    boolean compile(Container container, int startIndex) {
        List<String> resList = parseIf(container.rawLines, startIndex);
        container.rawLines = LineUtils.replaceRangeLine(container.rawLines, resList, startIndex, statementEndLine);
        return true;
    }

    private int conditionEndLine;
    private int statementStartLine;
    private int statementEndLine;

    //    startIndex : the index of the sentence containing if
    private List<String> parseIf(List<String> sentences, int startIndex) {

        // init
        conditionEndLine = 0;
        statementStartLine = 0;
        statementEndLine = 0;

        int lBracketPos = sentences.get(startIndex).indexOf("(");
        sentences.set(startIndex, sentences.get(startIndex).substring(lBracketPos+1));

        // 找到 if 紧跟的 ( 和 )，把中间的部分做成一个 condition。
        String conditionPart = findCondition(sentences, startIndex);
        // 将condition解析成list
        List<String> conditions = parseCondition(conditionPart, "&&");

        List<String> statements = parseStatement(sentences);

        List<String> resList = new ArrayList<>();
        resList.add("IF");
        resList.addAll(conditions);
        resList.add("THEN");
        resList.addAll(statements);
        resList.add("END");

        return resList;
    }

    private String findCondition(List<String> sentences, int startIndex) {
        int ctn = 1; // 因为已经去掉了头部的(，所以这里初始值=1
        for (int i = startIndex; i < sentences.size(); ++i) {
            char[] chs = sentences.get(i).toCharArray();
            for (int j = 0; j < chs.length; ++j) {
                if (chs[j] == '(') {
                    ++ctn;
                }
                else if (chs[j] == ')') {
                    if (ctn > 0) {
                        --ctn;
                    }
                    if (ctn == 0) {
                        conditionEndLine = i;
                        String condition = combineSentence(sentences, startIndex, 0, i, j);
                        return condition;
                    }
                }

            }
        }
        return "";
    }
    private String combineSentence(List<String> sentences, int from, int fromPos, int to, int toPos) {
        StringBuilder sb = new StringBuilder();
        if (from == to) {
            sb.append(sentences.get(from).substring(fromPos, toPos));
        } else {
            sb.append(sentences.get(from).substring(fromPos));
        }
        for (int i = from+1; i <= to; ++i) {
            if (i != to) {
                sb.append(sentences.get(i));
            } else {
                sb.append(sentences.get(to).substring(0, toPos));
            }
        }
        return sb.toString();
    }

    private List<String> parseCondition(String condition, String splitStr) {
        String[] arr = condition.split(splitStr);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < arr.length; ++i) {
            list.add(arr[i].trim());
        }
        return list;
    }

    private List<String> parseStatement(List<String> sentences) {

        int cnt = 0;
        for (int i = conditionEndLine; i < sentences.size(); ++i) {
            if (sentences.get(i).contains("{")) {
                statementStartLine = i;
                cnt++;
            }
            else if (sentences.get(i).contains("}")) {
                --cnt;
                if (cnt == 0) {
                    statementEndLine = i;
                    break;
                }
            }
        }

        List<String> statements = new ArrayList<>();
        for (int i = statementStartLine+1; i < statementEndLine; ++i) {
            statements.add(sentences.get(i));
        }
        return statements;
    }

}

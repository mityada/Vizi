<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet 
    version     = "1.0" 
    xmlns:xsl   = "http://www.w3.org/1999/XSL/Transform"
    xmlns:java  = "http://xml.apache.org/xalan/java"
>
    <xsl:output
        method      = "xml"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>

    <xsl:include href="common.xsl"/>

    <xsl:template match="algorithm">
        <xsl:call-template name="createFile"/>
    </xsl:template>

    <xsl:template name="getExtendsSection">
        <xsl:text>extends </xsl:text>
        <xsl:choose>
            <xsl:when test="count(//action)">
                <xsl:text>BaseAutoReverseAutomata</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>BaseAutomataWithListener</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="createConstructor">
        <method header="public {@id}(Locale locale)" comment="Конструктор для языка">
            <line value='super("{@package}.Comments", locale);'/>
            <line>init(new Main(), d);</line>
        </method>
    </xsl:template>

    <xsl:template match="algorithm/auto">
        <class 
            header="private final class {@id} extends BaseAutomata implements Automata" 
            comment="{@description}."
        >
            <variable comment="Начальное состояние автомата.">
                <xsl:text>private final int START_STATE = 0;</xsl:text>
            </variable>
            <variable comment="Конечное состояние автомата.">
                <xsl:text>private final int END_STATE = </xsl:text>
                <xsl:value-of select="1 + count(.//*[@state])"/>
                <xsl:text>;</xsl:text>
            </variable>

            <method 
                header  = "public {@id}()" 
                comment = "Конструктор."
            >
                <line>super(</line>
                <line value='    "{@id}",'/>
                <line>    0, // Номер начального состояния</line>
                <line value='    {1 + count(.//*[@state])}, // Номер конечного состояния'/>
                <line>    new String[]{</line>
                <line>        "Начальное состояние", </line>
                <xsl:for-each select=".//*[@state]">
                    <line value='        "{@description}",'/>
                </xsl:for-each>
                <line>        "Конечное состояние"</line>
                <line>    }, new int[]{</line>
                <line>        Integer.MAX_VALUE, // Начальное состояние, </line>
                <xsl:for-each select=".//*[@state]">
                    <xsl:choose>
                        <xsl:when test="name() != 'call-auto'">
                            <line value='        {@level}, // {@description}'/>
                        </xsl:when>
                        <xsl:otherwise>
                            <line value='        CALL_AUTO_LEVEL, // {@description}'/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
                <line>        Integer.MAX_VALUE, // Конечное состояние</line>
                <line>    }</line>
                <line>);</line>
            </method>
            <method 
                header  = "protected void doStepForward(int level)" 
                comment = "Сделать один шаг автомата в перед."
            >
                <xsl:call-template name="directStep"/>
                <xsl:call-template name="directAction"/>
            </method>
            <method 
                header  = "protected void doStepBackward(int level)" 
                comment = "Сделать один шаг автомата назад."
            >
                <xsl:call-template name="reverseAction"/>
                <xsl:call-template name="reverseStep"/>
            </method>

            <method header="public String getComment()" comment="Комментарий к текущему состоянию">
                <line>String comment = "";</line>
                <line>Object[] args = null;</line>
                <switch header="state" comment="Выбор комментария">
                    <xsl:apply-templates select=".//*[@state] | start | finish" mode="getComment"/>
                </switch>
                <line>return java.text.MessageFormat.format(comment, args);</line>
            </method>

            <method header="public void drawState()" comment="Выполняет действия по отрисовке состояния">
                <switch header="state">
                    <xsl:if test="start/draw">
                        <case label="START_STATE" comment="Начальное состояние">
                            <xsl:apply-templates select="start/draw"/>
                        </case>
                    </xsl:if>
                    <xsl:apply-templates mode="draw"/>
                    <xsl:if test="finish/draw">
                        <case label="END_STATE" comment="Конечное состояние">
                            <xsl:apply-templates select="finish/draw"/>
                        </case>
                    </xsl:if>
                </switch>
            </method>
        </class>
    </xsl:template>

    <xsl:template match="if[draw] | while[draw] | step[draw]" mode="draw">
        <case label="{@state}" comment="{@description}">
            <xsl:apply-templates select="draw"/>
        </case>
        <xsl:apply-templates mode="draw"/>
    </xsl:template>

    <xsl:template match="if[draw-true] | while[draw-true]" mode="draw">
        <case label="{@state}" comment="{@description}">
            <if>
                <xsl:attribute name="test">
                    <xsl:call-template name="convert">
                        <xsl:with-param name="text" select="@test"/>
                    </xsl:call-template>
                </xsl:attribute>
                <then>
                    <xsl:apply-templates select="draw-true"/>
                </then>
                <else>
                    <xsl:apply-templates select="draw-false"/>
                </else>
            </if>
        </case>
        <xsl:apply-templates mode="draw"/>
    </xsl:template>

    <xsl:template match="call-auto" mode="draw">
        <case label="{@state}" comment="{@description}">
            <line>child.drawState();</line>
        </case>
    </xsl:template>

    <xsl:template match="text()" mode="draw"/>

    <!--
        Get comment
    -->
    <xsl:template match="start" mode="getComment">
        <xsl:if test="@comment">
            <case label="START_STATE" comment="Начальное состояние">
                <line value='comment = {ancestor::algorithm/@id}.this.getComment("{ancestor::auto/@id}.START_STATE");'/>
                <xsl:if test="@comment-args">
                    <xsl:call-template name="madeArguments"/>
                </xsl:if>
            </case>
        </xsl:if>
    </xsl:template>

    <xsl:template name="madeArguments">
        <xsl:param name="node" select="."/>
        <line>
            <xsl:text>args = new Object[]{</xsl:text>
            <xsl:call-template name="convert">
                <xsl:with-param name="text" select="@comment-args"/>
            </xsl:call-template>
            <xsl:text>};</xsl:text>
        </line>
    </xsl:template>

    <xsl:template match="finish" mode="getComment">
        <xsl:if test="@comment">
            <case label="END_STATE" comment="Конечное состояние">
                <line value='comment = {ancestor::algorithm/@id}.this.getComment("{ancestor::auto/@id}.END_STATE");'/>
                <xsl:if test="@comment-args">
                    <xsl:call-template name="madeArguments"/>
                </xsl:if>
            </case>
        </xsl:if>
    </xsl:template>

    <xsl:template match="step" mode="getComment">
        <xsl:if test="@comment">
            <case label="{@state}" comment="{@description}">
                <line value='comment = {ancestor::algorithm/@id}.this.getComment("{ancestor::auto/@id}.{@id}");'/>
                <xsl:if test="@comment-args">
                    <xsl:call-template name="madeArguments"/>
                </xsl:if>
            </case>
        </xsl:if>
    </xsl:template>

    <xsl:template match="if|while|then" mode="getComment">
        <xsl:if test="@true-comment | @false-comment">
            <case label="{@state}" comment="{@description}">
                <if>
                    <xsl:attribute name="test">
                        <xsl:call-template name="convert">
                            <xsl:with-param name="text" select="@test"/>
                        </xsl:call-template>
                    </xsl:attribute>
                    <then>
                        <line value='comment = {ancestor::algorithm/@id}.this.getComment("{ancestor::auto/@id}.{@id}.true");'/>
                    </then>
                    <else>
                        <line value='comment = {ancestor::algorithm/@id}.this.getComment("{ancestor::auto/@id}.{@id}.false");'/>
                    </else>
                </if>
                <xsl:if test="@comment-args">
                    <xsl:call-template name="madeArguments"/>
                </xsl:if>
            </case>
        </xsl:if>
    </xsl:template>

    <xsl:template match="call-auto" mode="getComment">
        <case label="{@state}" comment="{@description}">
            <line value='comment = child.getComment();'/>
            <line value='args = new Object[0];'/>
        </case>
    </xsl:template>

    <!--
        Direct step
    -->
    <xsl:template name="directStep">
        <switch header="state" comment="Переход в следующее состояние">
            <xsl:variable name="firstNotStartNode" select="*[name() != 'start'][1]"/>
            <case label="START_STATE" comment="Начальное состояние">
                <xsl:if test="(name($firstNotStartNode)='while') and not($firstNotStartNode/@rtest)">
                    <line>stack.pushBoolean(false);</line>
                </xsl:if>
                <xsl:call-template name="setState">
                    <xsl:with-param name="state" select="*[@state][1]/@state"/>
                </xsl:call-template>
            </case>
            <xsl:for-each select=".//*[@state]">
                <case label="{@state}" comment="{@description}">
                    <xsl:apply-templates select="." mode="directStep"/>
                </case>
            </xsl:for-each>
        </switch>
    </xsl:template>

    <xsl:template match="step" mode="directStep">
        <xsl:call-template name="setNextState"/>
    </xsl:template>

    <xsl:template match="call-auto" mode="directStep">
        <if test="child.isAtEnd()">
            <then>
                <line>child = null;</line>
                <xsl:call-template name="setNextState"/>
            </then>
        </if>
    </xsl:template>

    <xsl:template match="if" mode="directStep">
        <if>
            <xsl:attribute name="test">
                <xsl:call-template name="convert">
                    <xsl:with-param name="text" select="@test"/>
                </xsl:call-template>
            </xsl:attribute>
            <then>
                <xsl:choose>
                    <xsl:when test="not(then/*)">
                        <line>stack.pushBoolean(true);</line>
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="then/@state"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="(name(then/*[1])='while') and not(*[1]/@rtest)">
                            <line>stack.pushBoolean(false);</line>
                        </xsl:if>
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="then/*[1]/@state"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </then>
            <else>
                <xsl:choose>
                    <xsl:when test="not(else/*)">
                        <line>stack.pushBoolean(false);</line>
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="then/@state"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:if test="(name(else/*[1])='while') and not(*[1]/@rtest)">
                            <line>stack.pushBoolean(false);</line>
                        </xsl:if>
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="else/*[1]/@state"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </else>
        </if>
    </xsl:template>

    <xsl:template match="then" mode="directStep">
        <xsl:for-each select="..">
            <xsl:call-template name="setNextState"/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="while" mode="directStep">
        <if>
            <xsl:attribute name="test">
                <xsl:call-template name="convert">
                    <xsl:with-param name="text" select="@test"/>
                </xsl:call-template>
            </xsl:attribute>
            <then>
                <xsl:if test="(name(*[@state][1])='while') and not(*[@state][1]/@rtest)">
                    <line>stack.pushBoolean(false);</line>
                </xsl:if>
                <xsl:call-template name="setState">
                    <xsl:with-param name="state" select="*[@state][1]/@state"/>
                </xsl:call-template>
            </then>
            <else>
                <xsl:call-template name="setNextState"/>
            </else>
        </if>
    </xsl:template>

    <!--
        Direct action
    -->
    <xsl:template name="directAction">
        <switch header="state" comment="Действие в текущем состоянии">
            <xsl:for-each select=".//*[@state]">
                <case label="{@state}" comment="{@description}">
                    <xsl:apply-templates select="." mode="directAction"/>
                </case>
            </xsl:for-each>
        </switch>
    </xsl:template>

    <xsl:template match="step" mode="directAction">
        <xsl:apply-templates select="direct"/>
        <xsl:apply-templates select="action" mode="directAction"/>
    </xsl:template>

    <xsl:template match="call-auto" mode="directAction">
        <if test="child == null">
            <then>
                <line value="child = new {@id}();"/>
                <line>child.toStart();</line>
            </then>
        </if>
        <line>child.stepForward(level);</line>
        <line>step--;</line>
    </xsl:template>

    <!-- TODO -->
    <xsl:template match="if" mode="directAction"/>

    <!-- TODO -->
    <xsl:template match="then" mode="directAction"/>

    <!-- TODO -->
    <xsl:template match="while" mode="directAction"/>

    <xsl:template match="action" mode="directAction">
        <xsl:text>startSection();</xsl:text>
        <xsl:text>&#10;</xsl:text>
        <xsl:call-template name="createDirectAction">
            <xsl:with-param name="text">
                <xsl:apply-templates select="."/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="createDirectAction">
        <xsl:param name="text"/>
        <xsl:choose>
            <xsl:when test="contains($text, '&#10;')">
                <xsl:call-template name="createDirectActionLine">
                    <xsl:with-param name="line" select="substring-before($text, '&#10;')"/>
                </xsl:call-template>
                <xsl:call-template name="createDirectAction">
                    <xsl:with-param name="text" select="substring-after($text, '&#10;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="createDirectActionLine">
                    <xsl:with-param name="line" select="$text"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="createDirectActionLine">
        <xsl:param name="line"/>
        <xsl:choose>
            <xsl:when test="contains($line, '@')">
                <xsl:variable name="head" select="substring-before($line, '@')"/>
                <xsl:variable name="tail" select="substring-after($line, '@')"/>
                <xsl:variable name="nhead" select="normalize-space($head)"/>
                <xsl:choose>
                    <xsl:when test="substring($nhead, string-length($nhead)) = ']'">
                        <xsl:variable name="index">
                            <xsl:call-template name="getIndex">
                                <xsl:with-param name="string" select="$nhead"/>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:call-template name="copyIndent">
                            <xsl:with-param name="line" select="$head"/>
                        </xsl:call-template>
                        <xsl:text>storeArray(</xsl:text>
                        <xsl:value-of select="substring($nhead, 1, $index)"/>
                        <xsl:text>, </xsl:text>
                        <xsl:value-of select="substring($nhead, $index + 2, string-length($nhead) - $index - 2)"/>
                        <xsl:text>);</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="copyIndent">
                            <xsl:with-param name="line" select="$head"/>
                        </xsl:call-template>
                        <xsl:text>storeField(</xsl:text>
                            <xsl:call-template name="beforeLastPeriod">
                                <xsl:with-param name="string" select="$nhead"/>
                            </xsl:call-template>
                        <xsl:text>, "</xsl:text>
                        <xsl:call-template name="afterLastPeriod">
                            <xsl:with-param name="string" select="$nhead"/>
                        </xsl:call-template>
                        <xsl:text>"</xsl:text>
                        <xsl:text>);</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:text>&#10;</xsl:text>
                <xsl:value-of select="$head"/>
                <xsl:value-of select="$tail"/>
                <xsl:text>&#10;</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$line"/>
                <xsl:text>&#10;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
        Reverse action
    -->
    <xsl:template name="reverseAction">
        <switch header="state" comment="Обращение действия в текущем состоянии">
            <xsl:for-each select=".//*[@state]">
                <case label="{@state}" comment="{@description}">
                    <xsl:apply-templates select="." mode="reverseAction"/>
                </case>
            </xsl:for-each>
        </switch>
    </xsl:template>

    <xsl:template match="step" mode="reverseAction">
        <xsl:apply-templates select="reverse"/>
        <xsl:apply-templates select="action" mode="reverseAction"/>
    </xsl:template>

    <xsl:template match="action" mode="reverseAction">
        <xsl:text>restoreSection();</xsl:text>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="call-auto" mode="reverseAction">
        <if test="child == null">
            <then>
                <line value="child = new {@id}();"/>
                <line>child.toEnd();</line>
            </then>
        </if>
        <line>child.stepBackward(level);</line>
        <line>step++;</line>
    </xsl:template>

    <!-- TODO -->
    <xsl:template match="if" mode="reverseAction"/>

    <!-- TODO -->
    <xsl:template match="then" mode="reverseAction"/>

    <!-- TODO -->
    <xsl:template match="while" mode="reverseAction"/>

    <!--
        Reverse step
    -->
    <xsl:template name="reverseStep">
        <switch header="state" comment="Переход в предыдущее состояние">
            <xsl:for-each select=".//*[@state]">
                <case label="{@state}" comment="{@description}">
                    <xsl:apply-templates select="self::*" mode="reverseStep"/>
                </case>
            </xsl:for-each>
            <case label="END_STATE" comment="Начальное состояние">
                <xsl:call-template name="setState">
                    <xsl:with-param name="state" select="*[@state][last()]/@endState"/>
                </xsl:call-template>
            </case>
        </switch>
    </xsl:template>

    <xsl:template match="step" mode="reverseStep">
        <xsl:call-template name="setPrevState"/>
    </xsl:template>

    <xsl:template match="call-auto" mode="reverseStep">
        <if test="child.isAtStart()">
            <then>
                <line>child = null;</line>
                <xsl:call-template name="setPrevState"/>
            </then>
        </if>
    </xsl:template>

    <xsl:template match="if" mode="reverseStep">
        <xsl:call-template name="setPrevState"/>
    </xsl:template>

    <xsl:template match="then" mode="reverseStep">
        <if>
            <xsl:attribute name="test">
                <xsl:choose>
                    <xsl:when test="not(../@rtest)">
                        <xsl:text>stack.popBoolean()</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="convert">
                            <xsl:with-param name="text" select="../@rtest"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>

            <then>
                <xsl:choose>
                    <xsl:when test="not(../then/*)">
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="../@state"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="../then/*[last()]/@endState"/>
                        </xsl:call-template>
                   </xsl:otherwise>
                </xsl:choose>
                <!--
                <xsl:call-template name="setState">
                    <xsl:with-param name="state" select="*[last()]/@endState"/>
                </xsl:call-template>
                -->
            </then>
            <else>
                <xsl:choose>
                    <xsl:when test="not(../else/*)">
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="../@state"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:call-template name="setState">
                            <xsl:with-param name="state" select="../else/*[last()]/@endState"/>
                        </xsl:call-template>
                   </xsl:otherwise>
                </xsl:choose>
            </else>
        </if>
    </xsl:template>

    <xsl:template match="while" mode="reverseStep">
        <if>
            <xsl:attribute name="test">
                <xsl:choose>
                    <xsl:when test="@rtest">
                        <xsl:call-template name="convert">
                            <xsl:with-param name="text" select="@rtest"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:text>stack.popBoolean()</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:attribute>
            <then>
                <xsl:call-template name="setState">
                    <xsl:with-param name="state" select="*[last()]/@endState"/>
                </xsl:call-template>
            </then>
            <else>
                <xsl:call-template name="setPrevState"/>
            </else>
        </if>
    </xsl:template>

    <!--
        Tools
    -->
    <xsl:template name="setState">
        <xsl:param name="state"/>
        <line value="state = {$state};">
            <xsl:attribute name="comment">
                <xsl:value-of select="ancestor-or-self::auto//*[@state = $state]/@description"/>
            </xsl:attribute>
        </line>
    </xsl:template>

    <xsl:template name="setNextState">
        <xsl:variable name="state">
            <xsl:choose>
                <xsl:when test="count(following-sibling::*[@state]) != 0">
                    <xsl:value-of select="following-sibling::*[1]/@state"/>
                </xsl:when>
                <xsl:when test="name(..) = 'auto'">
                    <xsl:text>END_STATE</xsl:text>
                </xsl:when>
                <xsl:when test="name(..) = 'while'">
                    <xsl:value-of select="../@state"/>
                </xsl:when>
                <xsl:when test="name(..) = 'then' or name(..) = 'else'">
                    <xsl:value-of select="../../@state + 1"/>
                </xsl:when>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="(name(following-sibling::*[1])='while') and not(following-sibling::*[1]/@rtest)">
                <line>stack.pushBoolean(false);</line>
            </xsl:when>
            <xsl:when test="not(following-sibling::*) and (parent::while) and not(../@rtest)">
                <line>stack.pushBoolean(true);</line>
            </xsl:when>
            <xsl:when test="not(following-sibling::*) and (parent::then) and not(../../@rtest)">
                <line>stack.pushBoolean(true);</line>
            </xsl:when>
            <xsl:when test="not(following-sibling::*) and (parent::else) and not(../../@rtest)">
                <line>stack.pushBoolean(false);</line>
            </xsl:when>
        </xsl:choose>
        <xsl:call-template name="setState">
            <xsl:with-param name="state" select="$state"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="setPrevState">
        <xsl:call-template name="setState">
            <xsl:with-param name="state">
                <xsl:choose>
                    <xsl:when test="count(preceding-sibling::*[@state])">
                        <xsl:value-of select="preceding-sibling::*[@state][1]/@endState"/>
                    </xsl:when>
                    <xsl:when test="name(..) = 'auto'">
                        <xsl:text>START_STATE</xsl:text>
                    </xsl:when>
                    <xsl:when test="name(..) = 'while'">
                        <xsl:value-of select="../@state"/>
                    </xsl:when>
                    <xsl:when test="name(..) = 'then' or name(..) = 'else'">
                        <xsl:value-of select="../../@state"/>
                    </xsl:when>
                </xsl:choose>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
</xsl:stylesheet>

<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:output
        method      = "xml"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>

    <xsl:include href="tools.xsl"/>

    <xsl:template match="algorithm">
        <xsl:call-template name="checkDeprecation"/>
        <algorithm>
            <xsl:copy-of select="ancestor-or-self::*/@*[namespace-uri() != 'http://www.w3.org/2001/XMLSchema-instance']"/>
            <xsl:copy-of select="import | variable | data | toString | method"/>
            <xsl:apply-templates select="auto" mode="algorithm"/>
        </algorithm>
    </xsl:template>

    <xsl:template match="start|finish" mode="algorithm">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="id">
                <xsl:choose>
                    <xsl:when test="name() = 'start'">
                        <xsl:text>START_STATE</xsl:text>
                    </xsl:when>
                    <xsl:when test="name() = 'finish'">
                        <xsl:text>END_STATE</xsl:text>
                    </xsl:when>
                </xsl:choose>
            </xsl:attribute>            
            <xsl:if test="@*[starts-with(name(), 'comment-')]">
                <!-- Marks that comment is present -->
                <xsl:attribute name="comment"/>
            </xsl:if>
            <xsl:copy-of select="*"/>
        </xsl:copy>        
    </xsl:template>

    <xsl:template match="step" mode="algorithm">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:if test="@*[starts-with(name(), 'comment')]">
                <xsl:attribute name="comment"/>
            </xsl:if>
            <xsl:if test="reverse">
                <xsl:attribute name="reverse"/>
            </xsl:if>
            <xsl:call-template name="insertStates"/>
            <xsl:copy-of select="*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="call-auto" mode="algorithm">
        <call-auto>
            <xsl:copy-of select="@*"/>

            <xsl:call-template name="insertStates"/>

            <xsl:if test="not(@description)">
                <xsl:attribute name="description">
                    <xsl:value-of select="//auto[@id = current()/@id]/@description"/>
                    <xsl:text> (автомат)</xsl:text>
                </xsl:attribute>
            </xsl:if>
        </call-auto>
    </xsl:template>

    <xsl:template match="if" mode="algorithm">
        <if>
            <xsl:copy-of select="@*"/>
            <xsl:if test="@*[starts-with(name(), 'true-comment-')]">
                <xsl:attribute name="true-comment"/>
            </xsl:if>
            <xsl:if test="@*[starts-with(name(), 'false-comment-')]">
                <xsl:attribute name="false-comment"/>
            </xsl:if>
            <xsl:variable name="state">
                <xsl:call-template name="getState"/>
            </xsl:variable>
            <xsl:attribute name="state">
                <xsl:value-of select="$state"/>
            </xsl:attribute>
            <xsl:attribute name="endState">
                <xsl:value-of select="$state + 1"/>
            </xsl:attribute>

            <xsl:copy-of select="draw|draw-true|draw-false"/>
            <then state="{$state + 1}" level="-1" description="{@description} (окончание)">
                <xsl:apply-templates select="then/*" mode="algorithm"/>
            </then>
            <else>
                <xsl:apply-templates select="else/*" mode="algorithm"/>
            </else>
        </if>
    </xsl:template>

    <xsl:template match="while" mode="algorithm">
        <while>
            <xsl:copy-of select="@*"/>

            <xsl:if test="@*[starts-with(name(), 'true-comment')]">
                <xsl:attribute name="true-comment"/>
            </xsl:if>
            <xsl:if test="@*[starts-with(name(), 'false-comment')]">
                <xsl:attribute name="false-comment"/>
            </xsl:if>

            <xsl:call-template name="insertStates"/>

            <xsl:copy-of select="draw|draw-true|draw-false"/>
            <xsl:apply-templates mode="algorithm"/>
        </while>
    </xsl:template>

    <!-- Recursion templates -->
    <xsl:template match="text()" mode="algorithm"/>

    <xsl:template match="*" mode="algorithm">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="algorithm"/>
        </xsl:copy>
    </xsl:template>

    <!-- Named templates -->
    <xsl:template name="insertStates">
        <xsl:variable name="state">
            <xsl:call-template name="getState"/>
        </xsl:variable>
        <xsl:attribute name="state">
            <xsl:value-of select="$state"/>
        </xsl:attribute>
        <xsl:attribute name="endState">
            <xsl:value-of select="$state"/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template name="getState">
        <xsl:param name="node" select="."/>

        <xsl:variable name="auto-id" select="$node/ancestor::auto/@id"/>
        <xsl:value-of select="
            count($node/preceding::step[ancestor::auto/@id = $auto-id]) +
            count($node/preceding::call-auto[ancestor::auto/@id = $auto-id]) +
            count($node/preceding::if[ancestor::auto/@id = $auto-id]) * 2 +
            count($node/ancestor ::if[ancestor::auto/@id = $auto-id]) * 2 + 
            count($node/preceding::while[ancestor::auto/@id = $auto-id]) +
            count($node/ancestor ::while[ancestor::auto/@id = $auto-id]) + 
            1
        "/>
    </xsl:template>

    <!-- Deprecation checks -->
    <xsl:template name="checkDeprecation">
        <xsl:param name="algorithm" select="."/>
        <xsl:if test="$algorithm/@type">
            <xsl:call-template name="showDeprecationMessage">
                <xsl:with-param name="message" select="'Attribute algorithm/@type is deprecated'"/>
            </xsl:call-template>
        </xsl:if>
        <xsl:for-each select="$algorithm/data/variable">
            <xsl:call-template name="showDeprecationMessage">
                <xsl:with-param name="message" select="'Deprecated variable declaration'"/>
                <xsl:with-param name="where" select="'Deprecated variable declaration'"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="showDeprecationMessage">
        <xsl:param name="message"/>
        <xsl:param name="where" select="."/>
        <xsl:message>
            <xsl:text>&#10;&#10;==================================================&#10;</xsl:text>
            <xsl:value-of select="$message"/>
            <xsl:text>&#10;--------------------------------------------------&#10;At </xsl:text>
            <xsl:for-each select="ancestor-or-self::*">
                <xsl:value-of select="name(.)"/>
                <xsl:if test="position() != last()">
                    <xsl:text>/</xsl:text>
                </xsl:if>
            </xsl:for-each>
            <xsl:text>&#10;==================================================&#10;&#10;</xsl:text>
        </xsl:message>
    </xsl:template>
</xsl:stylesheet>

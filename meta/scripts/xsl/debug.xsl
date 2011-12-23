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

    <xsl:template name="getExtendsSection"/>
    <xsl:template name="createConstructor"/>

    <xsl:template match="algorithm">
        <xsl:call-template name="createFile">
            <xsl:with-param name="suffix" select="'Debug'"/>
            <xsl:with-param name="variables">
                <variable header="public final AutoStack stack = new AutoStack();" comment="Стек."/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="algorithm/auto">
        <method
            header="private final void {@id}()" 
            comment="{@description}."
        >
            <xsl:apply-templates/>
        </method>
    </xsl:template>

    <xsl:template match="step">
        <line value='// {@description}'/>
        <xsl:apply-templates select="direct"/>
        <xsl:apply-templates select="action" mode="directAction"/>
    </xsl:template>

    <xsl:template match="while">
        <while comment="{@description}">
            <xsl:attribute name="test">
                <xsl:call-template name="convert">
                    <xsl:with-param name="text" select="@test"/>
                </xsl:call-template>
            </xsl:attribute>
            <xsl:apply-templates/>
        </while>
    </xsl:template>

    <xsl:template match="if">
        <if comment="{@description}">
            <xsl:attribute name="test">
                <xsl:call-template name="convert">
                    <xsl:with-param name="text" select="@test"/>
                </xsl:call-template>
            </xsl:attribute>
            <then>
                <xsl:apply-templates select="then/*"/>
            </then>
            <xsl:if test="else/*">
                <else>
                    <xsl:apply-templates select="else/*"/>
                </else>
            </xsl:if>
        </if>
    </xsl:template>

    <xsl:template match="call-auto">
        <line value='{@id}();'/>
    </xsl:template>

    <xsl:template match="action" mode="directAction">
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
                <xsl:value-of select="$head"/>
                <xsl:value-of select="$tail"/>
                <xsl:text>&#10;</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$line"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="draw"/>
</xsl:stylesheet>

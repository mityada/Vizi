<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:output
        method      = "text"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />

    <xsl:include href="tools.xsl"/>

    <xsl:param name="language"/>

    <xsl:template match="visualizer">
        <xsl:text>@echo off</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>rem </xsl:text>
        <xsl:call-template name="replace-nl">
            <xsl:with-param name="string" select="@*[name()=concat('name-', $language)]"/>
        </xsl:call-template>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>rem (C) </xsl:text>
        <xsl:value-of select="@*[name()=concat('author-', $language)]"/>
        <xsl:text> (</xsl:text>
        <xsl:value-of select="@author-email"/>
        <xsl:text>)</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>java -jar </xsl:text>
        <xsl:value-of select="@id"/>
        <xsl:text>.jar </xsl:text>
        <xsl:value-of select="$language"/>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>
</xsl:stylesheet>

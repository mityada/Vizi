<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:output
        method      = "text"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>

    <xsl:include href="tools.xsl"/>

    <xsl:template match="visualizer">
        <xsl:text>id=</xsl:text>
        <xsl:value-of select="@id"/>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>package=</xsl:text>
        <xsl:value-of select="@package"/>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>package.path=</xsl:text>
        <xsl:value-of select="translate(@package, '.', '/')"/>
        <xsl:text>/</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>preferred.width=</xsl:text>
        <xsl:value-of select="@preferred-width"/>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>preferred.height=</xsl:text>
        <xsl:value-of select="@preferred-height"/>
        <xsl:text>&#10;</xsl:text>

        <xsl:text>main.class=</xsl:text>
        <xsl:value-of select="@main-class"/>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>
</xsl:stylesheet>

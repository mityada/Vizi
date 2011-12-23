<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >    
    <xsl:output
        method      = "text"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>

    <xsl:param name="package"/>
    <xsl:param name="class"/>
    <xsl:param name="width"/>
    <xsl:param name="height"/>

    <xsl:template match="/">
        <xsl:text>load-bundle1=</xsl:text>
        <xsl:value-of select="concat($package, '.Configuration')"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:text>load-bundle2=</xsl:text>
        <xsl:value-of select="concat($package, '.Localization')"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:text>visualizer-class=</xsl:text>
        <xsl:value-of select="$class"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:text>visualizer-width=</xsl:text>
        <xsl:value-of select="$width"/>
        <xsl:text>&#10;</xsl:text>
        <xsl:text>visualizer-height=</xsl:text>
        <xsl:value-of select="$height"/>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="text()"/>
</xsl:stylesheet>

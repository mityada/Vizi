<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >    
    <xsl:output
        method      = "text"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>

    <xsl:param name="language"/>

    <xsl:include href="tools.xsl"/>

    <xsl:template match="algorithm//*">
        <xsl:variable name="auto-id" select="ancestor-or-self::auto/@id"/>
        <xsl:call-template name="setLocalizedProperty">
            <xsl:with-param name="id" select="concat($auto-id, '.', @id)"/>
            <xsl:with-param name="attr" select="'comment'"/>
        </xsl:call-template>
        <xsl:call-template name="setLocalizedProperty">
            <xsl:with-param name="id" select="concat($auto-id, '.', @id, '.true')"/>
            <xsl:with-param name="attr" select="'true-comment'"/>
        </xsl:call-template>
        <xsl:call-template name="setLocalizedProperty">
            <xsl:with-param name="id" select="concat($auto-id, '.', @id, '.false')"/>
            <xsl:with-param name="attr" select="'false-comment'"/>
        </xsl:call-template>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template name="setLocalizedProperty">
        <!-- Id of the generated property -->
        <xsl:param name="id"/>
        <!-- Prefix of the attribute node that contains property value -->
        <xsl:param name="attr"/>
        <xsl:call-template name="setProperty">
            <xsl:with-param name="id" select="$id"/>
            <xsl:with-param name="attr" select="concat($attr, '-', $language)"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="text()"/>
</xsl:stylesheet>

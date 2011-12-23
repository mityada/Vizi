<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:output
        method      = "html"
        indent      = "no"
        encoding    = "WINDOWS-1251"
    />

    <xsl:include href="tools.xsl"/>

    <xsl:param name="language"/>
    <xsl:param name="version"/>
    <xsl:param name="width"/>
    <xsl:param name="height"/>

    <xsl:template match="visualizer">
        <html>
            <head>
                <title><xsl:value-of select="@id"/><xsl:text> visualizer</xsl:text></title>
            </head>
            <body>
                <h3>
                    <xsl:call-template name="replace-nl">
                        <xsl:with-param name="string" select="@*[name()=concat('name-', $language)]"/>
                    </xsl:call-template>
                </h3>
                <xsl:value-of select="@*[name()=concat('author-', $language)]"/>
                <br/>
                <a href="{@author-email}">
                    <xsl:value-of select="@author-email"/>
                </a>
                <br/>
                <br/>
                <applet
                    code="ru.ifmo.vizi.base.AppletView"
                    width="{$width}"
                    height="{$height}"
                    archive="{@id}.jar, vizi-{$version}.jar"
                >
                    <param name="locale" value="{$language}"/>
                </applet>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>

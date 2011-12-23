<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet 
    version     = "1.0" 
    xmlns:xsl   = "http://www.w3.org/1999/XSL/Transform"
    xmlns:java  = "http://xml.apache.org/xalan/java"
>
    <xsl:output
        method      = "html"
        indent      = "no"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>

    <xsl:param name="language"/>
    <xsl:include href="tools.xsl"/>

    <xsl:template match="algorithm">
        <html>
            <head>
                <title><xsl:value-of select="@id"/><xsl:text> visualizer statistics</xsl:text></title>
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
                <table border="1">
                    <tr>
                        <th colspan="2">Общая статистика</th>
                    </tr><tr>
                        <td>Количество автоматов</td>
                        <td><xsl:value-of select="2 * count(auto)"/></td>
                    </tr><tr>
                        <td>Количество пар автоматов</td>
                        <td><xsl:value-of select="count(auto)"/></td>
                    </tr><tr>
                        <td>Количество состояний</td>
                        <td><xsl:value-of select="2 * count(auto) + count(.//*[@state])"/></td>
                    </tr><tr>
                        <th colspan="2">Количество состояний в автоматах</th>
                    </tr>
                    <xsl:for-each select="auto">
                        <tr>
                            <td><xsl:value-of select="@id"/></td>
                            <td><xsl:value-of select="count(.//*[@state]) + 2"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

    <!--
    <xsl:template match="algorithm">
        <visualizer automatas="{count(auto)}" states="{2 * count(auto) + count(.//*[@state])}">
            <xsl:for-each select="auto">
                <auto id="{@id}" states="{count(.//*[@state]) + 2}"/>
            </xsl:for-each>
        </visualizer>
    </xsl:template>
    -->
</xsl:stylesheet>

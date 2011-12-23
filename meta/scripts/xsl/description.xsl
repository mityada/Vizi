<?xml version = "1.0" encoding="WINDOWS-1251"?>
<!--
<!DOCTYPE xsl:stylesheet [
    <!ENTITY nbsp "<xsl:text>&#xA0;</xsl:text>">
]>
-->

<xsl:stylesheet 
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
>    
    <xsl:output
        method      = "html"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />

    <xsl:variable name="indent">
        <xsl:text>&#xA0;</xsl:text>
        <xsl:text>&#xA0;</xsl:text>
        <xsl:text>&#xA0;</xsl:text>
        <xsl:text>&#xA0;</xsl:text>
    </xsl:variable>
    <xsl:variable name="title" select="'The description of the configuration'"/>
    <xsl:variable name="style" select="document('css.xml')/style/text()"/>

    <xsl:template match="text()"/>

    <xsl:template match="//configuration">
        <html>
            <head>
                <title>
                    <xsl:value-of select="$title"/>
                </title>
                <style type="text/css">
                    <xsl:comment>
                        <xsl:copy-of select="$style"/>
                    </xsl:comment>
                </style>                    
            </head>
            <h1>
                <xsl:value-of select="$title"/>
            </h1>
            <table border="1" cellspacing="0" cellpadding="2" align="center">
                <tr>
                    <th>Parameter</th> 
                    <th>Description</th>
                </tr>
                <xsl:apply-templates select="group"/>
                <xsl:apply-templates select="styleset"/>
                <xsl:apply-templates select="panel"/>
                <xsl:apply-templates select="spin-panel"/>
                <xsl:apply-templates select="adjustablePanel"/>
                <xsl:variable name="tempSet" select="style | font | color | property | message | button | choice"/>
                <xsl:if test="count($tempSet) &gt; 0">
                    <tr> 
                        <th colspan="2">Additional parameters</th>
                    </tr>                
                    <!-- Because of style@param is just IMPLIED -->
                    <xsl:apply-templates select="style"/>
                    <xsl:apply-templates select="font | color | property | message | button | choice"/>
                </xsl:if>
            </table>
        </html> 
    </xsl:template>

    <xsl:template match="group">
        <xsl:param name="parent"/>
        <xsl:variable name="child" select="concat($parent, $indent)"/>
        <xsl:choose>
            <xsl:when test="string-length($parent) &gt;= string-length($indent)">
                <tr class="light">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:when>
            <xsl:otherwise>
                <tr class="dark">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="group">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="styleset">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="panel">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="spin-panel">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="adjustablePanel">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="style">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>

        <xsl:apply-templates select="font|color|property|message|button|choice">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>

    </xsl:template>

    
    <xsl:template match="styleset">
        <xsl:param name="parent"/>
        <xsl:variable name="child" select="concat($parent, $indent)"/>
        <xsl:choose>
            <xsl:when test="string-length($parent) &gt;= string-length($indent)">
                <tr class="light">
                    <td>
                        <b>
                            <xsl:value-of select="concat($parent, @param)"/>
                        </b>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:when>
            <xsl:otherwise>
                <tr class="dark">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:variable name="set" select="style"/>
        <xsl:for-each select="$set">
            <xsl:call-template name="style">            
                <xsl:with-param name="parent" select="$child"/>
                <xsl:with-param name="defaultParam" select="position() - 1"/>
            </xsl:call-template>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="style" name="style">
        <xsl:param name="parent"/>
        <!-- //style@param is IMPLIED so we must check its existance -->
        <xsl:param name="defaultParam" select="$indent"/>
        <tr>
            <td>
                <tt>
                    <xsl:choose>
                        <xsl:when test="($defaultParam != $indent)">
                            <xsl:value-of select="concat($parent, '-style', $defaultParam)"/>
                        </xsl:when>
                        <xsl:when test="normalize-space(@param) = ''">
                            <xsl:value-of select="$defaultParam"/>
                        </xsl:when>
                        <xsl:otherwise>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </tt>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="not(@description) or normalize-space(@description)=''">
                        <xsl:text>&#xA0;</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="normalize-space(@description)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </td>   
        </tr>
    </xsl:template>

    <xsl:template match="spin-panel">
        <xsl:param name="parent"/>
        <xsl:choose>
            <xsl:when test="string-length($parent) &gt;= string-length($indent)">
                <tr class="light">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:when>
            <xsl:otherwise>
                <tr class="dark">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="button">
            <xsl:with-param name="parent" select="concat($parent, $indent)"/> 
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="adjutablePanel">
        <xsl:param name="parent"/>
        <xsl:choose>
            <xsl:when test="string-length($parent) &gt;= string-length($indent)">
                <tr class="light">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:when>
            <xsl:otherwise>
                <tr class="dark">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates select="button">
            <xsl:with-param name="parent" select="concat($parent, $indent)"/> 
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="font|color|property|message|button|choice">
        <xsl:param name="parent"/>
        <tr>
            <td>
                <tt>
                    <xsl:value-of select="concat($parent, '-', @param)"/>
                </tt>
            </td>
            <td>
                <xsl:choose>
                    <xsl:when test="not(@description) or normalize-space(@description)=''">
                        <xsl:text>&#xA0;</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="normalize-space(@description)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>                   
    </xsl:template>

    <xsl:template match="panel">
        <xsl:param name="parent"/>
        <xsl:choose>
            <xsl:when test="string-length($parent) &gt;= string-length($indent)">
                <tr class="light">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>                   
            </xsl:when>
            <xsl:otherwise>
                <tr class="dark">
                    <td>
                        <tt>
                            <b>
                                <xsl:value-of select="concat($parent, '-', @param)"/>
                            </b>
                        </tt>
                    </td>
                    <td>
                        <xsl:choose>
                            <xsl:when test="not(@description) or normalize-space(@description)=''">
                                <xsl:text>&#xA0;</xsl:text>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:value-of select="normalize-space(@description)"/>
                            </xsl:otherwise>
                        </xsl:choose>
                    </td>
                </tr>                   
            </xsl:otherwise>
        </xsl:choose>
        <xsl:variable name="child" select="concat($parent, $indent)"/>
        <xsl:apply-templates select="font">
            <xsl:with-param name="parent" select="$child"/>
        </xsl:apply-templates>
    </xsl:template>

</xsl:stylesheet>

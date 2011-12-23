<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:template name="normalizeText">
        <xsl:param name="text"/>

        <xsl:call-template name="removeLines">
            <xsl:with-param name="text">
                <xsl:call-template name="removeIndent">
                    <xsl:with-param name="text" select="$text"/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="removeLines">
        <xsl:param name="text"/>
        
        <xsl:if test="normalize-space($text) != ''">
            <xsl:choose>
                <xsl:when test="not(contains($text, '&#10;'))">
                    <xsl:value-of select="$text"/>
                </xsl:when>
                <xsl:when test="normalize-space(substring-before($text, '&#10;')) = ''">
                    <xsl:call-template name="removeLines">
                        <xsl:with-param name="text" select="substring-after($text, '&#10;')"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="removeTrailingLines">
                        <xsl:with-param name="text" select="$text"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template name="removeTrailingLines">
        <xsl:param name="text"/>

        <xsl:if test="normalize-space($text) != ''">
            <xsl:choose>
                <xsl:when test="contains($text, '&#10;')">
                    <xsl:value-of select="substring-before($text, '&#10;')"/>
                    <xsl:variable name="tail">
                        <xsl:call-template name="removeTrailingLines">
                            <xsl:with-param name="text" select="substring-after($text, '&#10;')"/>
                        </xsl:call-template>
                    </xsl:variable>
                    <xsl:if test="normalize-space($tail) != ''">
                        <xsl:text>&#10;</xsl:text>
                        <xsl:value-of select="$tail"/>
                   </xsl:if>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$text"/>
                </xsl:otherwise>
            </xsl:choose>                
        </xsl:if>
    </xsl:template>

    <xsl:template name="removeIndent">
        <xsl:param name="text"/>

        <xsl:variable name="indent">
            <xsl:call-template name="countTextIndent">
                <xsl:with-param name="text" select="$text"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:call-template name="removeTextIndent">
            <xsl:with-param name="text" select="$text"/>
            <xsl:with-param name="indent" select="$indent"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="countTextIndent">
        <xsl:param name="text"/>
        
        <xsl:variable name="lineIndent">
            <xsl:call-template name="countLineIndent">
                <xsl:with-param name="line">
                    <xsl:choose>
                        <xsl:when test="contains($text, '&#10;')">
                            <xsl:value-of select="substring-before($text, '&#10;')"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$text"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:variable>

        <xsl:variable name="indent">
            <xsl:choose>
                <xsl:when test="contains($text, '&#10;')">
                    <xsl:call-template name="countTextIndent">
                        <xsl:with-param name="text" select="substring-after($text, '&#10;')"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>10000</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="$lineIndent &lt;= $indent">
                <xsl:value-of select="$lineIndent"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$indent"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="countLineIndent">
        <xsl:param name="line"/>

        <xsl:choose>
            <xsl:when test="normalize-space($line) = ''">
                <xsl:text>10000</xsl:text>
            </xsl:when>
            <xsl:when test="starts-with($line, ' ')">
                <xsl:variable name="indent">
                    <xsl:call-template name="countLineIndent">
                        <xsl:with-param name="line" select="substring($line, 2)"/>
                    </xsl:call-template>
                </xsl:variable>
                <xsl:value-of select="$indent + 1"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>0</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="removeTextIndent">
        <xsl:param name="text"/>
        <xsl:param name="indent"/>
        
        <xsl:choose>
            <xsl:when test="contains($text, '&#10;')">                
                <xsl:value-of select="substring(substring-before($text, '&#10;'), $indent + 1)"/>
                <xsl:text>&#10;</xsl:text>
                <xsl:call-template name="removeTextIndent">
                    <xsl:with-param name="text" select="substring-after($text, '&#10;')"/>
                    <xsl:with-param name="indent" select="$indent"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="addTextIndent">
        <xsl:param name="text"/>
        <xsl:param name="indent"/>
        
        <xsl:choose>
            <xsl:when test="normalize-space($text) = ''"/>
            <xsl:when test="contains($text, '&#10;')">                
                <xsl:value-of select="$indent"/>
                <xsl:value-of select="substring-before($text, '&#10;')"/>
                <xsl:text>&#10;</xsl:text>
                <xsl:call-template name="addTextIndent">
                    <xsl:with-param name="text" select="substring-after($text, '&#10;')"/>
                    <xsl:with-param name="indent" select="$indent"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$indent"/>
                <xsl:value-of select="$text"/>
                <xsl:text>&#10;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="setProperty">
        <!-- Id of the generated property -->
        <xsl:param name="id"/>
        <!-- Name of the attribute node that contains property value -->
        <xsl:param name="attr"/>
        <xsl:if test="count(@*[name()=$attr])">
            <xsl:value-of select="$id"/>
            <xsl:text>=</xsl:text>
            <xsl:value-of select="@*[name()=$attr]"/>
            <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="replace-nl">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="contains($string, '\n')">
                <xsl:value-of select="concat(normalize-space(substring-before($string, '\n')), ' ')"/>
                <xsl:call-template name="replace-nl">
                    <xsl:with-param name="string" select="substring-after($string, '\n')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
</xsl:stylesheet>

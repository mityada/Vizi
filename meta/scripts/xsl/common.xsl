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

    <xsl:include href="tools.xsl"/>

    <xsl:template name="createFile">
        <xsl:param name="suffix" select="''"/>
        <xsl:param name="variables"/>
        <file package="{@package}">
            <xsl:copy-of select="import"/>
            <import>ru.ifmo.vizi.base.auto.*</import>
            <import>java.util.Locale</import>
            <xsl:variable name="extends">
                <xsl:call-template name="getExtendsSection"/>
            </xsl:variable>
            <class header="public final class {@id}{$suffix} {$extends}">
                <xsl:copy-of select="method"/>

                <variable header="public final Data d = new Data();" comment="Модель данных."/>
                <xsl:copy-of select="$variables"/>

                <xsl:call-template name="createConstructor"/>

                <class header="public final class Data" comment="Данные.">
                    <xsl:for-each select="data/variable">
                        <!-- Deprecated variables declarations -->
                        <variable comment="{@description}.">
                            <xsl:text>public </xsl:text>
                            <xsl:call-template name="normalizeText">
                                <xsl:with-param name="text" select="text()"/>
                            </xsl:call-template>
                        </variable>
                    </xsl:for-each>
                    <xsl:for-each select="variable">
                        <variable comment="{@description}.">
                            <xsl:text>public </xsl:text>
                            <xsl:value-of select="@type"/>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="@name"/>
                            <xsl:text> = </xsl:text>
                            <xsl:value-of select="@value"/>
                            <xsl:text>;</xsl:text>
                        </variable>
                    </xsl:for-each>
                    <xsl:for-each select="auto/variable">
                        <variable comment="{@description} (Процедура {../@id}).">
                            <xsl:text>public </xsl:text>
                            <xsl:value-of select="@type"/>
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="../@id"/>
                            <xsl:text>_</xsl:text>
                            <xsl:value-of select="@name"/>
                            <xsl:text>;</xsl:text>
                        </variable>
                    </xsl:for-each>
                    <method header="public String toString()">
                        <xsl:choose>
                            <xsl:when test="toString">
                                <xsl:apply-templates select="toString/text()"/>
                            </xsl:when>
                            <xsl:when test="data/toString">
                                <xsl:message>
                                    <xsl:text>&#10;&#10;==================================================&#10;</xsl:text>
                                    <xsl:text>Deprecated toString usage.&#10;</xsl:text>
                                    <xsl:text>==================================================&#10;&#10;</xsl:text>
                                </xsl:message>
                                <xsl:apply-templates select="data/toString/text()"/>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:message terminate="yes">
                                    <xsl:text>&#10;&#10;==================================================&#10;</xsl:text>
                                    <xsl:text>toString is not specified.&#10;</xsl:text>
                                    <xsl:text>==================================================&#10;&#10;</xsl:text>
                                </xsl:message>
                            </xsl:otherwise>
                        </xsl:choose>
                    </method>
                </class>

                <xsl:apply-templates select="auto"/>
            </class>
        </file>
    </xsl:template>

    <xsl:template name="beforeLastPeriod">
        <xsl:param name="string"/>
        <xsl:value-of select="substring-before($string, '.')"/>
        <xsl:variable name="tail" select="substring-after($string, '.')"/>
        <xsl:if test="contains($tail, '.')">
            <xsl:text>.</xsl:text>
            <xsl:call-template name="beforeLastPeriod">
                <xsl:with-param name="string" select="$tail"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="afterLastPeriod">
        <xsl:param name="string"/>
        <xsl:choose>
            <xsl:when test="contains($string, '.')">
                <xsl:call-template name="afterLastPeriod">
                    <xsl:with-param name="string" select="substring-after($string, '.')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="copyIndent">
        <xsl:param name="line"/>
        <xsl:if test="starts-with($line, ' ')">
            <xsl:text> </xsl:text>
            <xsl:call-template name="copyIndent">
                <xsl:with-param name="line" select="substring($line, 2)"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="getIndex">
        <xsl:param name="string"/>
        <xsl:param name="count" select="0"/>
        <xsl:variable name="last" select="substring($string, string-length($string))"/>
        <xsl:variable name="head" select="substring($string, 1, string-length($string) - 1)"/>
        <xsl:choose>
            <xsl:when test="$last = ']'">
                <xsl:call-template name="getIndex">
                    <xsl:with-param name="string" select="$head"/>
                    <xsl:with-param name="count" select="$count + 1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="($last = '[') and ($count = 1)">
                <xsl:value-of select="string-length($head)"/>
            </xsl:when>
            <xsl:when test="($last = '[') and ($count != 1)">
                <xsl:call-template name="getIndex">
                    <xsl:with-param name="string" select="$head"/>
                    <xsl:with-param name="count" select="$count - 1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="getIndex">
                    <xsl:with-param name="string" select="$head"/>
                    <xsl:with-param name="count" select="$count"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="convert">
        <xsl:param name="text" select="."/>
        <xsl:value-of select="java:ViziTools.convert($text, ancestor::auto/@id, ancestor::algorithm/variable, ancestor::auto/variable)"/>
    </xsl:template>

    <xsl:template match="algorithm//text()" priority="1">
        <xsl:call-template name="normalizeText">
            <xsl:with-param name="text">
                <xsl:call-template name="convert">
                    <xsl:with-param name="text" select="."/>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="text()"/>
</xsl:stylesheet>

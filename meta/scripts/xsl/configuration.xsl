<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >    
    <xsl:output
        method      = "text"
        indent      = "no"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>
    <xsl:include href="tools.xsl"/>

    <xsl:param name="language" select="''"/>

    <xsl:variable name="nodes" select="document('configuration.dat.xml')/root/node"/>

    <xsl:template match="configuration">
        <xsl:apply-templates>
            <xsl:with-param name="prefix" select="''"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="group/property" priority="1"/>
    <xsl:template match="group/message" priority="1"/>

    <xsl:template match="configuration//group" priority="1">
        <xsl:param name="prefix"/>
        <xsl:choose>
            <xsl:when test="$language = ''">
                <xsl:if test="property">
                    <xsl:value-of select="concat($prefix, @param, '=')"/>
                    <xsl:for-each select="property">
                        <xsl:if test="@param">
                            <xsl:value-of select="concat('!', @param, ':')"/>
                        </xsl:if>
                        <xsl:call-template name="escape">
                            <xsl:with-param name="string" select="@value"/>
                        </xsl:call-template>
                    </xsl:for-each>
                    <xsl:text>&#10;</xsl:text>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="message">
                    <xsl:value-of select="concat($prefix, @param, '=')"/>
                    <xsl:for-each select="message">
                        <xsl:if test="@param">
                            <xsl:value-of select="concat('!', @param, ':')"/>
                        </xsl:if>
                        <xsl:call-template name="escape">
                            <xsl:with-param name="string" select="@*[name() = concat('message', '-', $language)]"/>
                        </xsl:call-template>
                    </xsl:for-each>
                    <xsl:text>&#10;</xsl:text>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:apply-templates>
            <xsl:with-param name="prefix" select="concat($prefix, @param, '-')"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="visualizer" priority="1">
        <xsl:call-template name="writeProperty">
            <xsl:with-param name="prefix" select="'about'"/>
            <xsl:with-param name="node" select="."/>
        </xsl:call-template>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="configuration//styleset" priority="1">
        <xsl:param name="prefix"/>
        <xsl:variable name="this" select="."/>

        <xsl:if test="$language = ''">
            <xsl:value-of select="concat($prefix, @param, '-styles=', count(style))"/>
            <xsl:text>&#10;</xsl:text>
        </xsl:if>
        <xsl:for-each select="style">
            <xsl:variable name="index" select="position() - 1"/>
            <xsl:apply-templates select=".">
                <xsl:with-param name="prefix" select="concat($prefix, $this/@param, '-style', $index)"/>
            </xsl:apply-templates>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="configuration//*">
        <xsl:param name="prefix"/>
        <xsl:call-template name="writeProperty">
            <xsl:with-param name="prefix" select="$prefix"/>
            <xsl:with-param name="node" select="."/>
        </xsl:call-template>
        <xsl:apply-templates>
            <xsl:with-param name="prefix" select="concat($prefix, @param, '-')"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template name="writeProperty">
        <xsl:param name="prefix"/>
        <xsl:variable name="node" select="."/>
        <xsl:choose>
            <xsl:when test="$language = ''">
                <xsl:if test="count($nodes[@name=name($node)]/param)">
                    <xsl:value-of select="concat($prefix, @param, '=')"/>
                    <xsl:for-each select="$nodes[@name=name($node)]/param">
                        <xsl:if test="$node/@*[name()=current()/@attr]">
                            <xsl:if test="@name">
                                <xsl:value-of select="concat('!', @name, ':')"/>
                            </xsl:if>
                            <xsl:call-template name="escape">
                                <xsl:with-param name="string" select="$node/@*[name()=current()/@attr]"/>
                            </xsl:call-template>
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:text>&#10;</xsl:text>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="count($nodes[@name=name($node)]/param-i18n)">
                    <xsl:value-of select="concat($prefix, @param, '=')"/>
                    <xsl:for-each select="$nodes[@name=name($node)]/param-i18n">
                        <xsl:if test="$node/@*[name()=concat(current()/@attr, '-', $language)]">
                            <xsl:if test="@name">
                                <xsl:value-of select="concat('!', @name, ':')"/>
                            </xsl:if>
                            <xsl:call-template name="escape">
                                <xsl:with-param name="string" select="$node/@*[name()=concat(current()/@attr, '-', $language)]"/>
                            </xsl:call-template>
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:text>&#10;</xsl:text>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="escape">
        <xsl:param name="string"/>
        <xsl:if test="string-length($string) > 0">
            <xsl:choose>
                <xsl:when test="starts-with($string, '!')">
                    <xsl:text>!!</xsl:text>
                </xsl:when>
                <xsl:when test="starts-with($string, ':')">
                    <xsl:text>::</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="substring($string, 1, 1)"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:call-template name="escape">
                <xsl:with-param name="string" select="substring($string, 2)"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="text()"/>
</xsl:stylesheet>

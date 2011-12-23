<?xml version = "1.0" encoding="WINDOWS-1251"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
    <xsl:output
        method      = "text"
        indent      = "yes"
        encoding    = "WINDOWS-1251"
    />
    <xsl:strip-space elements="*"/>

    <xsl:include href="tools.xsl"/>

    <xsl:template match="file">
        <xsl:if test="@package">
            <xsl:text>package </xsl:text>
            <xsl:value-of select="@package"/>
            <xsl:text>;</xsl:text>
            <xsl:text>&#10;</xsl:text>
            <xsl:text>&#10;</xsl:text>
        </xsl:if>
        <xsl:for-each select="import">
            <xsl:text>import </xsl:text>
            <xsl:value-of select="text()"/>
            <xsl:text>;</xsl:text>
            <xsl:text>&#10;</xsl:text>
        </xsl:for-each>
        <xsl:text>&#10;</xsl:text>

        <xsl:apply-templates select="class"/>
    </xsl:template>

    <xsl:template match="class">
        <xsl:call-template name="javadoc"/>

        <xsl:call-template name='indent'/>
        <xsl:value-of select="@header"/>
        <xsl:text> {</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:if test="count(following-sibling::*)"><xsl:text>&#10;</xsl:text></xsl:if>
    </xsl:template>

    <xsl:template match="variable">
        <xsl:call-template name="javadoc"/>

        <xsl:call-template name='indent'/>
        <xsl:value-of select="@header"/>
        <xsl:value-of select="text()"/>
        <xsl:text>&#10;</xsl:text>

        <xsl:if test="count(following-sibling::*)"><xsl:text>&#10;</xsl:text></xsl:if>
    </xsl:template>

    <xsl:template match="method">
        <xsl:call-template name="javadoc"/>

        <xsl:call-template name='indent'/>
        <xsl:value-of select="@header"/>
        <xsl:text> {</xsl:text>
        <xsl:text>&#10;</xsl:text>
        
        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:if test="count(following-sibling::*)"><xsl:text>&#10;</xsl:text></xsl:if>
    </xsl:template>

    <xsl:template match="switch">
        <xsl:call-template name="lineComment"/>

        <xsl:call-template name='indent'/>
        <xsl:text>switch (</xsl:text>
        <xsl:value-of select="@header"/>
        <xsl:text>) {</xsl:text>
        <xsl:text>&#10;</xsl:text>
        
        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:if test="count(following-sibling::*)"><xsl:text>&#10;</xsl:text></xsl:if>
    </xsl:template>

    <xsl:template match="case">
        <xsl:call-template name='indent'/>
        <xsl:text>case </xsl:text>
        <xsl:value-of select="@label"/>
        <xsl:text>: { </xsl:text>
        <xsl:call-template name="comment"/>

        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>    break;</xsl:text>
        <xsl:text>&#10;</xsl:text>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="simple-case">
        <xsl:call-template name='indent'/>
        <xsl:text>case </xsl:text>
        <xsl:value-of select="@label"/>
        <xsl:text>: </xsl:text>
        <xsl:call-template name="comment"/>

        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="if">
        <xsl:call-template name="lineComment"/>

        <xsl:call-template name='indent'/>
        <xsl:text>if (</xsl:text>
        <xsl:value-of select="@test"/>
        <xsl:text>) {</xsl:text>
        <xsl:text>&#10;</xsl:text>
        
        <xsl:apply-templates select="then"/>

        <xsl:if test="count(else)">
            <xsl:call-template name='indent'/>
            <xsl:text>} else {</xsl:text>
            <xsl:text>&#10;</xsl:text>

            <xsl:apply-templates select="else"/>
        </xsl:if>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="while">
        <xsl:call-template name="lineComment"/>

        <xsl:call-template name='indent'/>
        <xsl:text>while (</xsl:text>
        <xsl:value-of select="@test"/>
        <xsl:text>) {</xsl:text>
        <xsl:text>&#10;</xsl:text>
        
        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="synchronized">
        <xsl:call-template name="lineComment"/>

        <xsl:call-template name='indent'/>
        <xsl:text>synchronized (</xsl:text>
        <xsl:value-of select="@by"/>
        <xsl:text>) {</xsl:text>
        <xsl:text>&#10;</xsl:text>
        
        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="do-while">
        <xsl:call-template name="lineComment"/>

        <xsl:call-template name='indent'/>
        <xsl:text>do {</xsl:text>
        <xsl:text>&#10;</xsl:text>
        
        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>} while (</xsl:text>
        <xsl:value-of select="@test"/>
        <xsl:text>);</xsl:text>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="enumerate">
        <xsl:call-template name="lineComment"/>
        <xsl:variable name="variable">
            <xsl:choose>
                <xsl:when test="@variable">
                    <xsl:value-of select="@variable"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>e</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:call-template name='indent'/>
        <xsl:text>for (Enumeration </xsl:text>
        <xsl:value-of select="$variable"/>
        <xsl:text> = </xsl:text>
        <xsl:value-of select="@by"/>
        <xsl:text>; </xsl:text>
        <xsl:value-of select="$variable"/>
        <xsl:text>.hasMoreElements(); ) {</xsl:text>
        <xsl:text>&#10;</xsl:text>
        
        <xsl:apply-templates/>

        <xsl:call-template name='indent'/>
        <xsl:text>}</xsl:text>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template match="line">
        <xsl:call-template name='indent'/>
        <xsl:value-of select="@value"/>
        <xsl:value-of select="text()"/>
        <xsl:text> </xsl:text>
        <xsl:call-template name="comment"/>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:call-template name="addTextIndent">
            <xsl:with-param name="text" select="."/>
            <xsl:with-param name="indent"><xsl:call-template name='indent'/></xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="comment">
        <xsl:param name="comment" select="@comment"/>
        
        <xsl:if test="normalize-space($comment) != ''">
            <xsl:text>// </xsl:text>
            <xsl:value-of select="$comment"/>
        </xsl:if>
        <xsl:text>&#10;</xsl:text>
    </xsl:template>

    <xsl:template name="lineComment">
        <xsl:param name="comment" select="@comment"/>
        
        <xsl:if test="normalize-space($comment) != ''">
            <xsl:call-template name='indent'/>
            <xsl:text>// </xsl:text>
            <xsl:value-of select="$comment"/>
            <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="javadoc">
        <xsl:param name="comment" select="@comment"/>

        <xsl:if test="normalize-space($comment) != ''">
            <xsl:call-template name='indent'/>
            <xsl:text>/**</xsl:text>
            <xsl:text>&#10;</xsl:text>

            <xsl:call-template name="javadocLine">
                <xsl:with-param name="text" select="$comment"/>
            </xsl:call-template>

            <xsl:call-template name='indent'/>
            <xsl:text>  */</xsl:text>
            <xsl:text>&#10;</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="javadocLine">
        <xsl:param name="text"/>

        <xsl:choose>
            <xsl:when test="contains($text, '&#10;')">                
                <xsl:call-template name='indent'/>
                <xsl:text>  * </xsl:text>
                <xsl:value-of select="substring-before($text, '&#10;')"/>
                <xsl:text>&#10;</xsl:text>
                <xsl:call-template name="javadocLine">
                    <xsl:with-param name="text" select="substring-after($text, '&#10;')"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name='indent'/>
                <xsl:text>  * </xsl:text>
                <xsl:value-of select="$text"/>
                <xsl:text>&#10;</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="indent">
        <xsl:param name="indent" select="count(ancestor::*[name() != 'then'][name() != 'else']) - 1"/>

        <xsl:if test="$indent != 0">
            <xsl:text>    </xsl:text>
            <xsl:call-template name="indent">
                <xsl:with-param name="indent" select="$indent - 1"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>

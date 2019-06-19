package com.yshmgrt.chat.latex_extention
import androidx.annotation.Px
import ru.noties.markwon.ext.latex.JLatexMathBlock
import ru.noties.markwon.ext.latex.JLatexMathBlockParser





import android.graphics.drawable.Drawable
import android.net.Uri
import org.commonmark.node.Image
import org.commonmark.parser.Parser

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.util.Scanner

import ru.noties.jlatexmath.JLatexMathDrawable
import ru.noties.markwon.AbstractMarkwonPlugin
import ru.noties.markwon.MarkwonVisitor
import ru.noties.markwon.image.AsyncDrawableLoader
import ru.noties.markwon.image.ImageItem
import ru.noties.markwon.image.ImageProps
import ru.noties.markwon.image.ImageSize
import ru.noties.markwon.image.ImagesPlugin
import ru.noties.markwon.image.MediaDecoder
import ru.noties.markwon.image.SchemeHandler
import ru.noties.markwon.priority.Priority

/**
 * @since 3.0.0
 */
 class LatexPlugin internal constructor(private val config:Config):AbstractMarkwonPlugin() {

 interface BuilderConfigure {
 fun configureBuilder(builder:Builder)
}

 class Config internal constructor(builder:Builder) {
     val textSize:Float

 val background:Drawable?

@JLatexMathDrawable.Align
 val align:Int

 val fitCanvas:Boolean

 val padding:Int

init{
this.textSize = builder.textSize
this.background = builder.background
this.align = builder.align
this.fitCanvas = builder.fitCanvas
this.padding = builder.padding
}
}

override fun configureParser(builder:Parser.Builder) {
builder.customBlockParserFactory(JLatexMathBlockParser.Factory())
}

override fun configureVisitor(builder:MarkwonVisitor.Builder) {
builder.on(JLatexMathBlock::class.java!!
) { visitor, jLatexMathBlock ->
    val latex = jLatexMathBlock.latex()

    val length = visitor.length()
    visitor.builder().append(latex)

    val renderProps = visitor.renderProps()

    ImageProps.DESTINATION.set(renderProps, makeDestination(latex))
    ImageProps.REPLACEMENT_TEXT_IS_LINK.set(renderProps, false)
    ImageProps.IMAGE_SIZE.set(renderProps, ImageSize(ImageSize.Dimension(100f, "%"), null))

    visitor.setSpansForNode(Image::class.java!!, length)
}
}

override fun configureImages(builder:AsyncDrawableLoader.Builder) {
builder
.addSchemeHandler(SCHEME, object:SchemeHandler() {
override fun handle(raw:String, uri:Uri):ImageItem? {

var item:ImageItem? = null

try
{
val bytes = raw.substring(SCHEME.length).toByteArray(charset("UTF-8"))
item = ImageItem(
CONTENT_TYPE,
ByteArrayInputStream(bytes))
}
catch (e:UnsupportedEncodingException) {
e.printStackTrace()
}

return item
}
})
.addMediaDecoder(CONTENT_TYPE, object:MediaDecoder() {
override fun decode(inputStream:InputStream):Drawable? {

val scanner = Scanner(inputStream, "UTF-8").useDelimiter("\\A")
val latex = (if (scanner.hasNext())
    scanner.next()
else
    null) ?: return null

    return JLatexMathDrawable.builder(latex!!)
.textSize(config.textSize)
.background(config.background!!)
.align(config.align)
.fitCanvas(config.fitCanvas)
.padding(config.padding)
.build()
}
})
}

override fun priority():Priority {
return Priority.after(ImagesPlugin::class.java!!)
}

 class Builder internal constructor( val textSize:Float) {

 var background:Drawable? = null

@JLatexMathDrawable.Align
 var align = JLatexMathDrawable.ALIGN_CENTER

 var fitCanvas = true

 var padding:Int = 0

 fun background(background:Drawable):Builder {
this.background = background
return this
}

 fun align(@JLatexMathDrawable.Align align:Int):Builder {
this.align = align
return this
}

 fun fitCanvas(fitCanvas:Boolean):Builder {
this.fitCanvas = fitCanvas
return this
}

 fun padding(@Px padding:Int):Builder {
this.padding = padding
return this
}

 fun build():Config {
return Config(this)
}
}

companion object {

 fun create(textSize:Float):LatexPlugin {
return LatexPlugin(builder(textSize).build())
}

 fun create(config:Config):LatexPlugin {
return LatexPlugin(config)
}

 fun create(textSize:Float, builderConfigure:BuilderConfigure):LatexPlugin {
val builder = Builder(textSize)
builderConfigure.configureBuilder(builder)
return LatexPlugin(builder.build())
}
 fun builder(textSize:Float):LatexPlugin.Builder {
return Builder(textSize)
}

 fun makeDestination(latex:String):String {
return "$SCHEME://$latex"
}

 val SCHEME = "jlatexmath"
 val CONTENT_TYPE = "text/jlatexmath"
}
}
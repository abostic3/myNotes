package com.consumer.notesapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.lightColors

private val LightColors = lightColors(
    primary = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    background = md_theme_light_background,
    surface = md_theme_light_surface,
    onBackground = md_theme_light_onBackground,
    onSurface = md_theme_light_onSurface
)

@Composable
fun NotesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColors,
        typography = NotesTypography,
        shapes = NotesShapes,
        content = {
            Surface {
                content()
            }
        }
    )
}


## Learning project for Kotlin Android Automotive Bootcamp

Project consist of the following parts:
- Main screen with side navigation, which will be the entry point to the application, and through which the users will access other features (**launcher_app module**)
- Media player, should fulfill 2 main tasks, exposing media player as Media Browser service, and having media player that will be consuming other Media Browsing services ()
- Climate control and settings (**hvac_control**)

The project structure should conform to clean architecture principles ([Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html))  
The base proposed layers are (*but not limited to only them*):
- domain
- data
- presentation

Project should split to modules based on features, not layers. Every module should be divided to layers accordingli to clean architecture principles. Modules without UI might have a bit different separation on layers.

![app-modules-overview](https://bitbucket.org/hwhy/intellihmi/raw/0fd5e27bede4457ac98bf0864a3b2e2b55c6e8ff/intelli-auto-hmi/readme-assets/modules.png)


### Launcher App
Implement UI for switching between main app features

### Media player (media player consumer UI)
Implement a Fragment to display GridView of Artworks with Artist, Album, Title
Implement Models and Local Repository to retrieve local media files, read media metadata from the file and map to Models, store in local DB (Room)
Implement a Fragment to display music Artworks with Artist, Title, Song duration (playback screen)
Implement UI control to select media sources (local, online, media service)
Implement Media Browser Service consumer

### Media player (media browser service)
Implement Media Browser Service with Local media sources
Implement Media Browser Service with Online media sources

### Climate Control feature
-

### Settings feature
-


### Resources
- [Building media browser service](https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowserservice)
- [Building a media browser client](https://developer.android.com/guide/topics/media-apps/audio-app/building-a-mediabrowser-client)
- [Build media apps for cars](https://developer.android.com/training/cars/media)
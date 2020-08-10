# Roadmap/Planned
Should probably use a project board. Don't actually have a roadmap here.

#### UI
- [x] Menu Bar (94%)
    - [x] Quick Tools(83%)
        - [x] Open Minecraft Launcher(66%)
        - [x] Open MultiMC Launcher(100%)
    - [x] Language Selection (100%)
    - [x] Check for Updates (100%)
- [x] Mod Library Manager (50%)
    - [x] Drag and Drop Fabric Mods (100%)
    - [x] Enable/Disable Fabric Mods (100%)
    - [x] Eliminate Fabric Mods (100%)
        - [x] Can Eliminate (100%)
        - [x] Confirmation Menu(100%)
    - [x] Open Mods Folder (100%)
    - [x] Refresh Directory Mods (100%)
    - [ ] Mod Info redirect to `Mod Repository Browser` via specific title/author or minecraft version
    - [ ] Single Mod Updater (5%)
        - [x] Use ModUpdater(10%)
        - [ ] Use Existing Repository(0%)
    - [ ] Mod Pack Manager (0%)
        - [ ] ~~Custom Modpacks Profiles (0%)~~
        - [ ] Mod Pack Updater (0%)
    - [ ] Search Bar (0%)
    
- [x] Mod Repository Browser (60%)
    - [x] Search by title/author or minecraft version (100%)
    - [x] Minecraft and Mod Versions Selection (100%)
    - [x] Download/Install to specific profile or Downloads Manager (25%)
    - [ ] Build via GitHub Repository (no promises and bandwidth intensive)

- [ ] Downloads Manager (0%)
    - [ ] Install to specific profile or keep in the cache (0%)
    - [x] Asynchronous downloader (100%) using [jddl](https://github.com/kamranzafar/jddl)
    
- [ ] Settings (0%)
    - [ ] ~~Change Custom Minecraft Directory (0%)~~
    - [ ] Change MultiMC Directory(100%)
    - [ ] ~~Add/Remove Custom Repositories (0%)~~
        - [ ] ~~Direct JSON DB (5%)~~
        - [x] API (75%) using `CurseForge API`


#### ~~API (Future, missing resources to provide this. This is necessary for huge repositories)~~
~~This will be most likely written in `NodeJS`.~~

- [ ] ~~Mod Repository API (5%)~~
    - [x] ~~Schematics (75%)~~
    - [ ] ~~Query via GraphQL (no promises)~~
   
- [x] ~~Mod Repository Local API (10%) meant for [Fabric Mod Repository](https://github.com/FlashyReese/fabric-mod-repository).~~

#### Database
~~This will be most likely split into 2. Using `GitHub` as repository which will make the application memory intensive. Using `MongoDB` as an actual database and query using the API. How do I plan to populate these? Well no idea.~~
- [ ] ~~Mod Repository using [Fabric Mod Repository](https://github.com/FlashyReese/fabric-mod-repository).~~ I will be keeping this for testing might keep it as an update strategy.
- [x] Curse Forge API. **NOTE**: This will only allow projects that are opted in.

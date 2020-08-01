# Roadmap/Planned
Should probably use a project board. Don't actually have a roadmap here.

#### UI
- [x] Menu Bar (??%)
    - [x] Check for Updates (50%)
        - Need to thread completely forgot
- [x] Mod Library Manager (50%)
    - [x] Drag and Drop Fabric Mods (100%)
    - [x] Enable/Disable Fabric Mods (100%)
    - [x] Eliminate Fabric Mods (50%)
        - [x] Can Eliminate (50%)
        - [ ] Confirmation Menu(0%)
    - [x] Open Mods Folder (100%)
    - [x] Refresh Directory Mods (100%)
    - [ ] Mod Info redirect to `Mod Repository Browser` via specific title/author or minecraft version
    - [ ] Single Mod Updater `requires Mod Repository API` (0%)
    - [ ] Mod Pack Manager (0%)
        - [ ] Custom Modpacks Profiles (0%)
        - [ ] Mod Pack Updater `requires Mod Repository API` (0%)
    - [ ] Search Bar (0%)
    
- [ ] Mod Repository Browser (0%)
    - [ ] Search by title/author or minecraft version (0%)
    - [ ] Mod Versions Selection (0%)
    - [ ] Download/Install to specific profile or Downloads Manager (0%)
    - [ ] Build via GitHub Repository (no promises and bandwidth intensive)

- [ ] Downloads Manager (0%)
    - [ ] Install to specific profile or keep in the cache (0%)
    - [ ] Asynchronous downloader (0%)
    
- [ ] Settings (0%)
    - [ ] Change Custom Minecraft Directory (0%)
    - [ ] Add/Remove Custom Repositories (0%)
        - [ ] Direct JSON DB (0%)
        - [ ] API (0%)


#### API
This will be most likely written in `NodeJS`.

- [ ] Mod Repository API (5%)
    - [x] Schematics (75%)
    - [ ] Query via GraphQL (no promises)


#### Database
This will be most likely split into 2. Using `GitHub` as repository which will make the application memory intensive. Using `MongoDB` as an actual database and query using the API. How do I plan to populate these? Well no idea.
- [ ] Mod Repository (0%)
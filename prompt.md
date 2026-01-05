Goal:
Create an Android application that functions as a local note‑taking app. All notes must be stored on the device, not in the cloud. Users must be able to create, edit, and permanently delete notes.

📝 Note Types & Behaviors
1. Free‑Form Markdown Note
- Supports Markdown formatting.
- Editable at any time.
- Stored locally.
2. Bulleted List Note
- Default format is a bulleted list.
- User can toggle between bulleted list and numbered list.
- Tapping a bullet/number should strike out the entire line.
- Stored locally.
3. Secret Notes
- A separate section of notes.
- Access requires phone PIN authentication (use Android’s biometric/PIN authentication APIs).
- Secret notes behave like normal notes but are stored in a protected area (encrypted storage preferred).

📂 Navigation & UI Requirements
Left‑Hand Navigation Drawer
Include the following menu items:
- New Note
- See Notes
- See Secret Notes
  General UI Expectations
- Modern Material Design components.
- Smooth transitions between note types.
- Clear visual distinction between normal notes and secret notes.

💾 Storage Requirements
- All notes must be stored locally on the device.
- Use Room or another local persistence solution.
- Secret notes should be encrypted or stored in a secure location.
- Support full CRUD operations:
- Create
- Read
- Update
- Permanently Delete

🧩 Implementation Expectations
- Kotlin preferred.
- MVVM architecture recommended.
- Use Jetpack components where appropriate (Room, Navigation, ViewModel, LiveData/Flow).
- Provide sample UI layouts and data models.
- Include code for:
- Markdown editor
- List editor with bullet/number toggle
- Strike‑through behavior on bullet tap
- PIN authentication for secret notes
- Navigation drawer setup

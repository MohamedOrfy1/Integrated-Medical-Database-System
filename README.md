# Git Branching Strategy

## Branching Hierarchy
```plaintext
main  ←  test  ←  backend  ←  feature/backend-auth
               ←  frontend  ←  feature/frontend-ui
```
- `main` → Production-ready branch (stable releases only).
- `test` → Main development branch where `backend` & `frontend` merge before release.
- `backend` → Branch for backend team development.
- `frontend` → Branch for frontend team development.
- `feature/*` → Temporary branches for specific features or bug fixes.

---

## Workflow Guide
### 1️- Cloning the Repository
```sh
git clone git@github.com:MohamedOrfy1/Integrated-Medical-Database-System.git
cd Integrated-Medical-Database-System
git checkout test  # Start from the test branch
```

### 2️⃣ Creating a Feature Branch
**For Backend Developers:**
```sh
git checkout backend  # Switch to backend branch
git pull origin backend  # Ensure it's up to date
git checkout -b feature/backend-auth  # Create a new feature branch
```
**For Frontend Developers:**
```sh
git checkout frontend  # Switch to frontend branch
git pull origin frontend  # Ensure it's up to date
git checkout -b feature/frontend-ui  # Create a new feature branch
```

### 3️⃣ Committing & Pushing Changes
```sh
git add .
git commit -m "Implemented authentication system"
git push origin feature/backend-auth  # Push to remote
```

### 4️⃣ Creating a Pull Request (PR)
1. Go to **GitHub** → Open a **Pull Request (PR)** from `feature/backend-auth` → `backend`.
2. Wait for code review & approval.
3. Merge into `backend` when approved.

### 5️⃣ Merging Backend & Frontend into Test
Once multiple features are merged into `backend` or `frontend`, merge them into `test`:
```sh
git checkout test
git pull origin test
git merge backend  # Merge backend changes
git merge frontend  # Merge frontend changes
git push origin test  # Push updates
```

---

## Best Practices
- Always pull the latest changes before starting work.
- Keep feature branches **small & focused** (1 feature per branch).
-  **Do not push directly** to `test` or `main`.
- Delete feature branches after merging to keep the repo clean.

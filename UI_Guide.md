# Bank-Z: Professional UI Guide

## 1. Purpose of This Guide

This guide defines the **User Interface (UI) standards** for Bank-Z.
It ensures that developers, designers, and testers follow a consistent approach when building screens, styling components, and handling user interactions.

---

## 2. General Design Principles

- **Simplicity:** Keep layouts clean, uncluttered, and easy to navigate.
- **Consistency:** Reuse styles, components, and interaction patterns.
- **Accessibility:** Ensure text is readable, buttons are large enough, and colors have enough contrast.
- **Feedback:** Every action (login, transfer, update) should give clear visual or textual feedback.
- **Separation of Concerns:** Use **FXML** for layout, **CSS** for styling, and **Controllers** for logic.

---

## 3. Layout Standards

- **Window Size:** Default window 1280×720, resizable.
- **Navigation:**

  - Customers: Sidebar navigation (Dashboard, Accounts, Transfer, Deposit/Withdraw).
  - Admins: Sidebar navigation (Dashboard, Customers, Accounts, Transactions).

- **Spacing:** Minimum **16px padding** between sections, **8px spacing** between related items.
- **Alignment:** Left-align text, right-align action buttons.
- **Whitespace:** Avoid overcrowding; use whitespace to separate sections.

---

## 4. Color Palette

| Element         | Color Code | Usage                                    |
| --------------- | ---------- | ---------------------------------------- |
| Primary Blue    | `#1E88E5`  | Headers, buttons, highlights             |
| Secondary Gray  | `#F5F5F5`  | Backgrounds                              |
| Accent Green    | `#43A047`  | Success states (deposits, confirmations) |
| Warning Orange  | `#FB8C00`  | Alerts, warnings                         |
| Error Red       | `#E53935`  | Errors, failed actions                   |
| Text Dark Gray  | `#212121`  | Primary text                             |
| Text Light Gray | `#757575`  | Secondary text                           |

---

## 5. Typography

- **Font Family:** Segoe UI / Roboto (fallback: Arial, sans-serif)
- **Font Sizes:**

  - Titles: 20–24px, bold
  - Section Headers: 16–18px, semi-bold
  - Body Text: 14–16px
  - Captions/Labels: 12–14px

---

## 6. Components

### 6.1 Buttons

- **Primary Button:** Filled blue background, white text.
  Example: **Login**, **Confirm Transfer**.
- **Secondary Button:** Transparent background, blue border, blue text.
- **Disabled State:** Light gray background, gray text.
- **Hover State:** Slightly darker background.

### 6.2 Input Fields

- Rounded corners, light gray background, border `#CCCCCC`.
- Focused: Border changes to primary blue.
- Error: Border changes to error red with inline message.

### 6.3 Tables (Accounts, Transactions, Customers)

- Header row: Bold, light gray background.
- Alternating row colors for readability.
- Search/filter bar above each table.

### 6.4 Dialogs/Popups

- Centered modal with overlay background.
- Used for transfers, confirmation prompts, and error alerts.
- Must have **Clear CTA** (e.g., "Confirm" / "Cancel").

---

## 7. Screen Guidelines

### 7.1 Login Screen

- **Unified login** for customers and admins.
- Fields: Username, Password.
- Primary button: **Login**.
- Error handling: Show inline message (e.g., _Invalid username or password_).

### 7.2 Dashboard

- **Customer Dashboard:** Overview of accounts, balances, recent 5 transactions.
- **Admin Dashboard:** Quick stats (number of customers, total accounts, recent system transactions).

### 7.3 Accounts View

- Table with account number, type, balance, status.
- Action buttons: **View Transactions**, **Close/Freeze** (for admins).

### 7.4 Transaction History

- Paginated table.
- Columns: Date, Type, Amount, Source Account, Destination Account, Description.

### 7.5 Fund Transfer

- Dropdown for **Source Account**.
- Input for **Destination Account Number**.
- Input for **Amount**.
- Button: **Confirm Transfer**.
- Success/Failure popup messages.

### 7.6 Customer Management (Admin)

- Table: customer ID, name, username, date created.
- Actions: **View/Edit**, **Create New Customer**.

---

## 8. Interaction & Feedback Rules

- **Loading Indicators:** Show progress for long actions (e.g., transfers).
- **Success:** Use green confirmation popups or banners.
- **Error:** Red inline messages or popup with explanation.
- **Validation:** Prevent invalid inputs (negative amounts, empty fields) before submission.
- **Navigation Highlight:** Active section highlighted in sidebar.

---

## 9. Accessibility Guidelines

- Minimum contrast ratio 4.5:1 for text.
- Keyboard shortcuts for major actions (e.g., `Ctrl+N` for new customer).
- All interactive elements must be reachable by **Tab** key.

---

## 10. Example CSS Snippet

```css
/* Buttons */
.button-primary {
  -fx-background-color: #1e88e5;
  -fx-text-fill: white;
  -fx-font-weight: bold;
  -fx-padding: 8 16;
  -fx-background-radius: 6;
}
.button-primary:hover {
  -fx-background-color: #1565c0;
}

/* Input Fields */
.text-field {
  -fx-background-color: #f5f5f5;
  -fx-border-color: #cccccc;
  -fx-padding: 6;
}
.text-field:focused {
  -fx-border-color: #1e88e5;
}
.text-field:error {
  -fx-border-color: #e53935;
}
```

---

## 11. Testing UI Consistency

- Test all screens in **light and dark environments**.
- Verify resizing doesn’t break layout.
- Check color contrast meets accessibility rules.
- Ensure error/success states are clearly visible.

---

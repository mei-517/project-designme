# Product Requirements Document

## Product

Project DesignMe


---

## Purpose

予定を登録するだけではなく、
自分の時間を自由にデザインできる
カレンダーアプリを作る。


---

# Target Users


## Primary Users

### Students

大学・アルバイト・勉強など、
複数の予定を管理する学生。

Needs

- 授業やシフトを簡単に登録したい
- 勉強時間を管理したい
- 月全体の時間の使い方を見たい

### Shift Workers

勤務時間が固定ではなく、
毎週予定が変化する人。

Needs

- シフト登録を簡単にしたい
- 給料を確認したい

---

## Secondary Users
### Business Users

会議・作業時間・予定を
細かく管理したい社会人。


### Freelancers

複数案件や作業時間を管理する人。


---


# Functional Requirements
## 1. View Management


### View Switching

User can:

- 複数の表示形式を切り替えできる
- 同じ予定データを目的に合わせて違う形で表示できる


Views:

- Month View
- Time Design View
- Week View
- Day View


Purpose:

予定を見るだけではなく、
時間の使い方を直感的に把握する。


---


## 2. Month View


Purpose:

一般的なカレンダー形式で、
予定全体を確認する。


User can:

- 月単位で予定を見る
- 日付ごとの予定量を確認する
- 日付をタップしてDay Viewを開く
- Time Design Viewへ切り替える


---


## 3. Time Design View


Purpose:

1ヶ月または2週間の時間の使い方を
一覧で確認する。


User can:

- 日ごとの時間軸を並べて表示する
- 予定を時間ブロックとして確認する
- 空き時間を視覚的に確認する
- Month Viewへ切り替える


Display Options:

- 1 Month
- 2 Weeks


---


## 4. Day View


Purpose:

1日の予定を細かく編集する。


User can:

- 時間軸上で予定を確認する
- ドラッグして予定を追加する
- 予定を伸縮して時間変更する
- 予定を移動する
- 予定をコピーする
- 予定を削除する


Important:

予定編集専用ページを作らず、
カレンダー上で直接操作する。


---


## 5. Event Management


### Create Event


User can:

- 最小限の入力で予定を作成できる


Required:

- Title
- Time


Optional:

- Color
- Notification
- Repeat
- Memo


Purpose:

予定登録のストレスを減らす。


---


### Edit Event


User can:

- 名前変更
- 時間変更
- 色変更
- 通知設定
- 繰り返し設定


---


## 6. Drag Operation


Purpose:

スマホ操作に近い直感的な編集を実現する。


User can:

- ドラッグで予定作成
- ドラッグで時間変更
- 長押しでコピー
- スライドで別日に移動


---


## 7. Time Snap


Purpose:

予定入力速度と細かさを調整する。


User can:

時間単位を変更できる。


Options:

- 1 hour
- 30 minutes
- 15 minutes
- 10 minutes
- 5 minutes
- 1 minute


---


## 8. Template Management


Purpose:

繰り返し使う予定入力を高速化する。


Examples:

- University Class
- Part Time Job
- Meeting
- Study


User can:

- 固定時間の予定テンプレートを作る
- 開始時間だけ変更して追加する
- 繰り返し予定として登録する


---


## 9. Shift Management


Purpose:

予定管理と収入管理をまとめる。


User can:

- シフト登録
- 時給設定
- 勤務時間計算
- 月給確認


---


## 10. Memo Management


User can:

- 日付ごとにメモを書く
- 画像を添付する


---


## 11. Calendar Export


Purpose:

作った予定表を日常で使える形にする。


User can:

- Month Viewを画像保存する
- スマホ壁紙として利用する


Status:

Future Feature


---


# Non Functional Requirements



---


# MVP



---


# Future Ideas
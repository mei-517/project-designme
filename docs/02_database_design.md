# Database Design

## Purpose

予定（イベント）を保存するためのテーブル設計をまとめる。

最初に作るのは Month View（月表示）なので、
月表示に必要な最小限の項目だけを扱う。

Database: PostgreSQL


---


# Overview

最初のテーブルは `events`（予定）の1つだけ。

ログイン機能はまだ作らないため、
ユーザーを管理するテーブルは今は作らない。
（→ Future Extensions 参照）


扱う予定は2種類ある。

### 時間指定の予定

例：10/20 10:00〜12:00 授業

- 1日の中で開始時刻と終了時刻がある
- 日付をまたぐ予定（22:00〜翌2:00 など）は扱わない


### 終日の予定

例：10/24〜10/25 旅行

- 時刻を持たない
- 1日だけでも、複数日にまたがってもよい


---


# Table Definition

## events

| カラム名 | 型 | 必須 | 説明 |
|---|---|---|---|
| `id` | `BIGINT` | ○ | 予定を見分けるための番号（自動採番） |
| `title` | `VARCHAR(100)` | ○ | 予定のタイトル |
| `start_date` | `DATE` | ○ | 開始日 |
| `end_date` | `DATE` | ○ | 終了日（この日を含む） |
| `is_all_day` | `BOOLEAN` | ○ | 終日の予定なら `TRUE` |
| `start_time` | `TIME` | − | 開始時刻（終日なら空） |
| `end_time` | `TIME` | − | 終了時刻（終日なら空） |
| `color` | `VARCHAR(7)` | ○ | 予定の表示色（例：`#3B82F6`） |
| `memo` | `TEXT` | − | メモ |
| `created_at` | `TIMESTAMP` | ○ | 作成日時 |
| `updated_at` | `TIMESTAMP` | ○ | 最終更新日時 |


---


# Column Details

### id

予定1件ごとに付く、重複しない番号。
「主キー（Primary Key）」と呼ばれる。

自分で番号を考えなくても、
登録するたびに DB が 1, 2, 3... と自動で振ってくれる。

`INT` ではなく `BIGINT` にしているのは、
扱える数の上限が大きく、件数が増えても困らないため。


### title

予定の名前。必須。

`VARCHAR(100)` は「最大100文字の文字列」という意味。
カレンダーのマスに表示する短い文字なので、上限を決めておく。


### start_date / end_date

予定の開始日と終了日。

`DATE` は「年月日だけ」を持つ型。（例：`2026-10-24`）

終了日は **その日を含む**。

| 予定 | start_date | end_date |
|---|---|---|
| 10/20 の授業 | 2026-10-20 | 2026-10-20 |
| 10/24〜10/25 の旅行 | 2026-10-24 | 2026-10-25 |

時間指定の予定は日付をまたがないので、
`start_date` と `end_date` は必ず同じ日になる。


### is_all_day

終日の予定かどうか。

`BOOLEAN` は `TRUE`（はい）か `FALSE`（いいえ）だけを持つ型。

- `TRUE`：終日の予定。時刻は持たない
- `FALSE`：時間指定の予定。時刻が必須

指定しなかった場合は `FALSE` になる。


### start_time / end_time

開始時刻と終了時刻。

`TIME` は「時分秒だけ」を持つ型。（例：`10:00:00`）

終日の予定では時刻がないので、空（`NULL`）にする。

`NULL` は「値が入っていない」ことを表す特別な値。
0 や空文字とは別物。


### color

月表示で予定を色分けするための色。

`#` + 16進数6桁のカラーコードで保存する。（例：`#3B82F6` は青）

`VARCHAR(7)` は「最大7文字の文字列」。
`#RRGGBB` がちょうど7文字なので、この長さにしている。

ユーザーが色を選ばなかった場合は、
初期値の `#3B82F6`（青）が入る。
必ず何かの色が入るので、画面側で「色がない場合」を考えなくてよい。


### memo

予定の補足メモ。空でもよい。

`TEXT` は長さの上限がない文字列。
メモは長くなることもあるので、上限を決めない。


### created_at / updated_at

いつ作られたか・いつ最後に変更されたかの記録。

画面には出さないが、
不具合調査や並び替えで役に立つので最初から入れておく。

`updated_at` は予定を編集するたびにアプリ側
（Spring Boot / JPA）で現在時刻に更新する。


---


# Rules

入力ミスで変なデータが保存されないように、
DB 側でもルールを決めておく。
このようなルールを「制約（Constraint）」と呼ぶ。


### 1. 終了日は開始日より前にならない

`end_date >= start_date`


### 2. 終日かどうかで時刻のルールが変わる

終日の予定（`is_all_day = TRUE`）：

- `start_time` と `end_time` は空


時間指定の予定（`is_all_day = FALSE`）：

- `start_time` と `end_time` は必須
- `start_date` と `end_date` は同じ日
- 終了時刻は開始時刻より後


### 3. 色は `#RRGGBB` の形式

`#` のあとに 0〜9 / A〜F が6文字続く形だけを許す。


アプリ側でもチェックするが、
DB でも守らせることで、バグがあっても壊れたデータが残らない。


---


# SQL

```sql
CREATE TABLE events (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    is_all_day  BOOLEAN      NOT NULL DEFAULT FALSE,
    start_time  TIME,
    end_time    TIME,
    color       VARCHAR(7)   NOT NULL DEFAULT '#3B82F6',
    memo        TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- ルール1：終了日は開始日以降
    CONSTRAINT chk_events_date_range
        CHECK (end_date >= start_date),

    -- ルール2：終日なら時刻なし / 時間指定なら同じ日の中で開始 < 終了
    CONSTRAINT chk_events_time
        CHECK (
            (is_all_day = TRUE
                AND start_time IS NULL
                AND end_time IS NULL)
            OR
            (is_all_day = FALSE
                AND start_time IS NOT NULL
                AND end_time IS NOT NULL
                AND start_date = end_date
                AND end_time > start_time)
        ),

    -- ルール3：色は #RRGGBB 形式
    CONSTRAINT chk_events_color
        CHECK (color ~ '^#[0-9A-Fa-f]{6}$')
);

-- 月表示で「その月の予定」を速く探すための索引
CREATE INDEX idx_events_date_range ON events (start_date, end_date);
```

### 用語メモ

- `NOT NULL`：空を許さない（必須）
- `DEFAULT`：値を指定しなかったときに入る初期値
- `GENERATED ALWAYS AS IDENTITY`：番号を自動で振る
- `INDEX`（索引）：本の索引のように、データを速く探すための仕組み
- `~`：文字列が決まった形（正規表現）に合っているかを調べる PostgreSQL の演算子


---


# Sample Data

```sql
-- 時間指定の予定
INSERT INTO events (title, start_date, end_date, is_all_day, start_time, end_time, color, memo)
VALUES ('授業', '2026-10-20', '2026-10-20', FALSE, '10:00', '12:00', '#10B981', '教室 301');

-- 1日だけの終日予定（色を指定しない → 初期値の青になる）
INSERT INTO events (title, start_date, end_date, is_all_day)
VALUES ('誕生日', '2026-10-22', '2026-10-22', TRUE);

-- 2日間の終日予定
INSERT INTO events (title, start_date, end_date, is_all_day, color, memo)
VALUES ('旅行', '2026-10-24', '2026-10-25', TRUE, '#F59E0B', '京都');
```


---


# Month View Query

2026年10月の月表示に必要な予定を取得する例。

```sql
SELECT *
FROM events
WHERE start_date <= '2026-10-31'
  AND end_date   >= '2026-10-01'
ORDER BY start_date, is_all_day DESC, start_time;
```

### なぜ「開始日が10月」で探さないのか

`start_date` が10月のものだけを探すと、
**9/30〜10/1 の旅行** のように、前の月から続く予定が漏れてしまう。

「月の最終日より前に始まり、月の初日より後に終わる」
＝ **10月と少しでも重なる予定** を探すことで、漏れなく取得できる。


### 並び順

1. 日付順
2. 同じ日なら終日の予定を先に（カレンダーの上側に表示するため）
3. 時間指定の予定は開始時刻順


---


# Design Decisions

### テーブルは1つにする

終日の予定と時間指定の予定を別テーブルに分ける方法もあるが、
月表示では両方をまとめて表示するので、1つのテーブルの方が扱いやすい。


### 日付と時刻を別々のカラムにする

`TIMESTAMP`（日付＋時刻）1つで持つ方法もある。

今回は

- 日付をまたぐ時間指定の予定は扱わない
- 終日の予定は時刻を持たない

ため、日付と時刻を分けた方がシンプルで、初心者にも読みやすい。


### 終了日は「その日を含む」にする

10/24〜10/25 の旅行は `end_date = 2026-10-25` と保存する。

「終了日の翌日」を保存する方式（Google Calendar などが採用）もあるが、
画面の表示と同じ値が DB に入る方がわかりやすいので、含む方式にする。


---


# Limitations

- 日付をまたぐ時間指定の予定（22:00〜翌2:00 など）は登録できない
- タイムゾーンは考慮しない（日本時間で使う前提）


---


# Future Extensions

PRD にあるが、今回はまだ入れない項目。

| 機能 | 追加する予定のもの |
|---|---|
| ログイン | `users` テーブル、`events.user_id` |
| 通知 | 通知設定のカラム、または `notifications` テーブル |
| 繰り返し | 繰り返しルールのテーブル |
| テンプレート | `templates` テーブル |
| シフト・時給 | シフト・時給を管理するテーブル |
| 日付ごとのメモ・画像 | `daily_memos` テーブル、画像の保存先 |

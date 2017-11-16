## AddItemActivity:
 * date and time label show the current date and time
 * when calculating date, directly take the value if of the label, if user don't pick a certain date and time,


## future task:
 * Location function not done.
 * data structure for database: adding new entity: NotificationSending

新功能：
 1. 可以添加过去的日期作为纪念日
 2. 顶部的状态栏始终为黑色
 3. 通知定时功能，系统通知，email模板

 已经知道的Bug：
 1. adapter空指针问题
 2. 如果选择的日期小于今天，不能添加日期 这个时候save了 报空指针bug
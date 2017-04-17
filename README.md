# myGitDemo
我的GitHub操作demo


---

git操作
#生成sshkey
git config --global user.name "wangxinghao"
git config --global user.email "wangxinghao@cloud-young.com"
ssh-keygen -t rsa -C "wangxinghao@cloud-young.com"


git基本命令：

%%初始化一个Git仓库
git init

%%添加文件到Git仓库
git add <file>
git commit

%%查看工作区的状态
git status

%%查看修改内容
git diff

%%查看历史记录
git log
git log --pretty=oneline

%%回退到上一个版本
git reset --hard HEAD^
(在Git中，用HEAD表示当前版本;上一个版本就是HEAD^，上上一个版本就是HEAD^^;往上100个版本写成HEAD~100)

%%查看命令历史，以便确定要回到未来的哪个版本
git reflog

%%查看工作区和版本库里面最新版本的区别
git diff HEAD -- filename

%%撤销修改
场景1：当你改乱了工作区某个文件的内容，想直接丢弃工作区的修改时，用命令git checkout -- file。

场景2：当你不但改乱了工作区某个文件的内容，还添加到了暂存区时，想丢弃修改，分两步，第一步用命令git reset HEAD file，就回到了场景1，第二步按场景1操作。

场景3：已经提交了不合适的修改到版本库时，想要撤销本次提交，参考版本回退一节，不过前提是没有推送到远程库。


%%删除一个文件
git rm <filename>

%%关联远程仓库前的准备：创建公钥，并导入
ssh-keygen -t rsa -C "fuzuokui@dangdang.com"

%%要关联一个远程库：
git remote add origin https://github.com/fuzuokui/learngit.git
(远程库的名字就是origin，这是Git默认的叫法，也可以改成别的)

%%第一次推送master分支的所有内容:
git push -u origin master

%%本地推送最新修改：
git push origin master

%%克隆一个本地库：
git clone git@github.com:fuzuokui/gitskills.git
（要克隆一个仓库，首先必须知道仓库的地址，然后使用git clone命令克隆）

%%查看分支：
git branch

%%创建分支：
git branch <name>

%%切换分支：
git checkout <name>

%%创建+切换分支：
git checkout -b <name>

%%合并某分支到当前分支：
git merge <name>

%%删除分支：
git branch -d <name>

%%查看分支的合并情况：
git log --graph --pretty=oneline --abbrev-commit

%%用普通模式合并分支：
git merge --no-ff -m "merge with no-ff" dev

%%把当前工作现场“储藏”起来
git stash

%%刚才的工作现场存到哪去了
git stash list

%%回到工作现场
git stash pop

%%丢弃一个没有被合并过的分支
git branch -D <name>

%%查看远程库信息
git remote -v

%%从本地推送分支
git push origin branch-name

%%抓取远程的新提交
git pull

%%在本地创建和远程分支对应的分支
git checkout -b branch-name origin/branch-name
（本地和远程分支的名称最好一致；）

%%建立本地分支和远程分支的关联
git branch --set-upstream branch-name origin/branch-name

%%打一个新标签
git tag <name> [<commit id>]

%%查看所有标签
git tag

%%找到历史提交的commit id
git log --pretty=oneline --abbrev-commit

%%查看标签信息
git show <tagname>

%%创建带有说明的标签，用-a指定标签名，-m指定说明文字
git tag -a v0.1 -m "version 0.1 released" 3628164

%%推送一个本地标签
git push origin <tagname>

%%推送全部未推送过的本地标签
git push origin --tags

%%删除一个本地标签
git tag -d <tagname>

%%删除一个远程标签
git push origin :refs/tags/<tagname>


%%多人协作的工作模式通常是这样：

    1、首先，可以试图用git push origin branch-name推送自己的修改；

    2、如果推送失败，则因为远程分支比你的本地更新，需要先用git pull试图合并；

    3、如果合并有冲突，则解决冲突，并在本地提交；

    4、没有冲突或者解决掉冲突后，再用git push origin branch-name推送就能成功！

		5、如果git pull提示“no tracking information”，则说明本地分支和远程分支的链接关系没有创建，用命令git branch --set-upstream branch-name origin/branch-name。


%%gitignore不起作用的解决方法
 .gitignore文件,具体的规则一搜就有.我在使用GIT的过程中,明明写好了规则,但问题不起作用,每次还是重复提交,无法忍受.其实这个文件里的规则对已经追踪的文件是没有效果的.
 所以我们需要使用rm命令清除一下相关的缓存内容.这样文件将以未追踪的形式出现.然后再重新添加提交一下,.gitignore文件里的规则就可以起作用了.
git rm -r --cached .
git add .
git commit -m 'update .gitignore'

%%git全局配置文件
C:\Users\xxx\.gitconfig


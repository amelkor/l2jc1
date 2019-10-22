====================
L2J Server v0.4
====================
$Date: 2004/10/21 14:53:10 $
$Revision: 1.9 $

TOC:
I.    OVERVIEW
II.   REQUIREMENTS
III.  FIRST STARTUP
IV.   ADMIN/GM's GUIDE
V.    PLAYER's GUIDE
VI.   UPDATING
VII.  TROUBLESHOOTING
VIII. LEGAL
IX.   CONTACT
X.    CONTRIBUTING
XI.   BUG REPORTING
XII.  CREDITS


====================
I. OVERVIEW
====================

L2J is an Alternative Lineage 2 Game Server written in pure Java for
best compatibility. L2J gives you the possibility to legally host game
server for this popular Korean MMO created by NCSoft. It is still
unfinished and many features are missing, but L2J Dev team is working
hard on implementing them. L2J Server is distributed under the terms
of GNU/GPL in a hope that open source model is the best for
developping quality software giving everyone a possibility to
participate on development by submitting the code.


====================
II. REQUIREMENTS
====================

OS: Any OS having Java JDK 1.4 installed and properly configured!
    We recommend using SUN JDK available at java.sun.com

HW: Decent CPU & RAM


====================
III. FIRST STARTUP
====================

Before you can start up the server you need to create some data files
from the client content. To do this, just startup the convertData.bat
(Windows) or convertData.sh (*nix) found in L2J Tools package. It will
ask you for your L2 install folder and create 5 files in the server
data folder.

This distribution does not contain any spawn/drop data or any world
content. Example files are included so you should be able to create
the data by yourself or you might find data packs somewhere on the
net.

L2J Server has also a possibility to change xp/sp/drop rates relatives
to data in spawnlist/droplist files. To do that, just change the rates 
you need in rates.csv file found in data folder.

You should also configure your IP address in server.cfg and adjust
logging level in log.cfg. See files below for examples and details.

Server uses ports 2106 (LoginServer) and 7777 (GameServer) by
default. If your server runs behind NAT or firewall you will need to
open/forward these ports. You may change GameServer port in server.cfg
to suit your needs, but it is not possible to change LoginServer port
because this one is hardcoded in all clients.


====================
IV. ADMIN/GM's GUIDE
====================

To make someone an admin you need to edit data/logins.txt file while
server is SHUT DOWN! Change last number on the line with login from 0
to 100 or more. You may start server after that and the person will
have admin privileges. Note that you must create the account before
editing :p Good solution is to run Account Manager included in tools
package.

Possible access levels in logins.txt:
-100 = account banned
0 = normal account
51 or more = extempt from maximum connections limit
100 or more = admin
 
Admin commands implemented:
//admin = main GM interface
//gmchat = will send a message to all online GMs
//invul = makes our character untouchable

====================
V. PLAYER's GUIDE
====================

CLIENT COMPATIBILITY
L2J should be compatible with any Chronicle 1 client with revision 417
or higher. Patched NA, Japanese or Korean clients were tested and
works fine.

Please note that Korea has switched to C2 already, so if you don't
have old C1 client you should download Japanese or NA one. Unpatched
Korean C1 client won't work as it's revision is 415.

L2J will support C2 in the future, but Dev team decided to have good &
working C1 support before starting C2 developpement.


====================
VI. UPDATING
====================
It may arrive that you want to update your server to new version while
keeping old accounts. There are few steps you HAVE TO do in order to
keep the data accurate.

- You should ALWAYS look at changelog before updating, sometimes a file
  format may change, so you will need to edit data manually to fit with
  new format.
- Backup all data under /data/accounts, /data/clans and /data/crests
- Backup data/logins.txt file
- BACKUP /data/idstate.dat FILE. THIS IS IMPERATIVE TO PREVENT SPAWNING
  OF SO CALLED "DUPED" ITEMS.
- if you ddon't want to re-run data conversion, backup following files
  from /data/ folder: armor.csv, etcitem.csv, npc.csv, skills.csv and
  weapon.csv (you should look if any update was done to L2_Tools, if so
  that it's recomended to generate data from scratch)
- back up all .cfg files (don't forget to check if new server use same 
  format for those files)
- download & unzip new server code
- restore backed data by overwriting any files needed (don't forget to
  check file formats!)
- run newly installed server & enjoy ;)


====================
VII. TROUBLESHOOTING
====================

PROBLEM
- Client outputs bunch of messages about missing templates.
SOLUTION
- Run convertData script.

PROBLEM
- Message similar to "java is not recognized as internal command",
"java not found" or "unknow command: java" appears.
SOLUTION
- Install java, or, if java is already installed just add your java
binary directory to system PATH setting. If you don't know how to do
that, than DO NOT bother running your own server please.

PROBLEM
- I can log in but ping is 9999s and I can't get past Server Select.
SOLUTION
- Set up your IP's properly, forward/open good ports if accessing from
outside. (or find server with admin that knows how to do it)

PROBLEM
- Skills/quests/whatever don't work.
SOLUTION
- Patience brings it's fruits :p

PROBLEM
- I found a bug.
SOLUTION
- Please refer to BUG REPORTING section of this readme.


====================
VIII. LEGAL
====================

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307,
USA.

Full GNU/GPL License is included in LICENSE.TXT file.

Whereas L2J is distributed under the terms of GNU/GPL, we will be
happy if you:
a) Preserve logon notice. This give us, L2J Developers, appropriate
credit for our hard work done during our free time without any
revenues.
 
b) Do not distribute any extended data files with the server files in
the same archive. NO world content should be incorporated in L2J
distribution.

Notes:
NO data extracted by dataConverter should be redistributed as this
data is copyrighted by NCSoft. You are free to use it by yourself, but
redistributing it could cause legal issues. If your friends want to
run L2J server, they will need to download compatible C1 client and
run convertData by themselves.


====================
IX. CONTACT
====================

Web: http://l2j.sourceforge.net
IRC: #l2j @ Freenode (irc.freenode.net)

Please note that L2J Devs can't help players with connecting issues or
anything related to playing on private servers. If you can't connect,
you should contact your server GM's. We can solve only L2J server
~software~ related issues. We don't have nay backdoors or anything
that would enable us GM accounts on every server using L2J, so there's
no point in coming to our channel if you need items/adenas/whatever
ingame.


====================
X. CONTRIBUTING
====================

Anyone who want to contribute to project is encouraged to do so. Java
programming skills are not always required as L2J needs much more than
java code.

If you created anything that may be helpful use User Contributions
section on our forums. If you contributed good stuff that will be
accepted, you will be invited to join L2J Dev Team.

People willing to hang on chat and respond to user questions are also
ALWAYS welcome ;)


====================
XI. BUG REPORTING
====================

Bugs can be reported on our forums in support section. Basic rules for
reporting are:
- post it in right section (0.3, 0.4, cvs etc)
- do not start new threads for bugs already reported (SEARCH before
posting). If you have something to precise, reply in existing thread.

Players should ALWAYS consult bugs with their GM's before reporting it
on our boards. Some bugs may be caused by bad datapack, server
installation or modification server owner has made. We can't help you
in that case.

Please do NOT report bugs related to unofficial add-ons to L2J. L2J
bugtracker is NOT a place to fix that. Contact the person who made
modification instead.


====================
XII. CREDITS
====================

Team leader:
~~~~~~~~~~~~
~~ L2Chef ~~
~~~~~~~~~~~~

Dev team (in alphabetical order):
+----------------------------------------------------------------+
| BlurCode, Dalrond, DETH-, err0rr, J3ster, lithium, NightMarez, |
| nuocnam, MetalRabbit, Tenkawa, whatev                          |
+----------------------------------------------------------------+
(this list contains also curretly inactive members)


Have fun playing Lineage II ;)
Dev Team
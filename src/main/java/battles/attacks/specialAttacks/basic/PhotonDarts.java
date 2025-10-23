package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.pixelmon.api.battles.BattleType;
import com.pixelmonmod.pixelmon.api.util.helpers.RandomHelper;
import com.pixelmonmod.pixelmon.battles.api.rules.BattleRuleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import java.util.ArrayList;

public class PhotonDarts extends SpecialAttackBase {
    public int minHits = 2;
    public int maxHits = 5;
    private transient int count = 0;
    private transient int targetCount = 2;
    private transient boolean inProgress = false;
    private transient int limit = 0;
    private transient boolean hasSecondTarget = false;
    private transient PixelmonWrapper firstTarget;

    public boolean repeatsAttack() {
        if (this.maxHits == 0) {
            if (this.count >= this.minHits) {
                return false;
            } else {
                ++this.count;
                return true;
            }
        } else if (this.count >= this.targetCount) {
            return false;
        } else {
            ++this.count;
            return true;
        }
    }

    public AttackResult applyEffectDuring(PixelmonWrapper user, PixelmonWrapper target) {
        if (this.limit++ <= 2 && this.inProgress) {
            return AttackResult.proceed;
        } else {
            user.inMultipleHit = true;
            this.inProgress = true;
            this.limit = 0;
            if (this.maxHits != 0) {
                this.targetCount = 2;
            }

            this.count = 0;
            int initAccuracy = user.attack.getMove().getAccuracy();
            boolean hasSubstitute = false;

            while(user.isAlive() && this.repeatsAttack()) {
                hasSubstitute = target.hasStatus(new StatusType[]{StatusType.Substitute});
                MoveResults[] results = user.useAttackOnly();

                for(MoveResults result : results) {
                    MoveResults var10000 = user.attack.moveResult;
                    var10000.damage += result.damage;
                    var10000 = user.attack.moveResult;
                    var10000.fullDamage += result.fullDamage;
                }

                if (!target.isAlly(user) && !this.hasSecondTarget) {
                    this.firstTarget = target;
                    PixelmonWrapper newTarget = this.selectDragonDartsTarget(user, target);
                    user.targets.remove(0);
                    user.targets.add(newTarget);
                    this.hasSecondTarget = true;
                }
            }

            user.attack.setMoveAccuracy(initAccuracy);
            user.inMultipleHit = false;
            this.inProgress = false;
            this.hasSecondTarget = false;
            user.attack.sendEffectiveChat(user, target);
            user.bc.sendToAll("multiplehit.times", new Object[]{user.getNickname(), this.count});
            Attack.postProcessAttackAllHits(user, target, user.attack, (float)user.attack.moveResult.damage, DamageTypeEnum.ATTACK, hasSubstitute);
            return AttackResult.hit;
        }
    }

    public void applyMissEffect(PixelmonWrapper user, PixelmonWrapper target) {
        PixelmonWrapper newTarget = this.selectDragonDartsTarget(user, target);
        if (newTarget != target) {
            PixelmonWrapper previousTarget = (PixelmonWrapper)user.targets.remove(0);
            if (!this.hasSecondTarget) {
                this.firstTarget = previousTarget;
            }

            if (this.hasSecondTarget) {
                if (previousTarget == this.firstTarget) {
                    return;
                }

                user.targets.add(this.firstTarget);
            } else {
                user.targets.add(newTarget);
            }

            this.hasSecondTarget = true;
            MoveResults[] results = user.useAttackOnly();

            for(MoveResults result : results) {
                MoveResults var10000 = user.attack.moveResult;
                var10000.damage += result.damage;
                var10000 = user.attack.moveResult;
                var10000.fullDamage += result.fullDamage;
                user.attack.moveResult.accuracy = result.accuracy;
            }
        }

    }

    private PixelmonWrapper selectDragonDartsTarget(PixelmonWrapper user, PixelmonWrapper target) {
        PixelmonWrapper newTarget = target;
        if (user.bc.rules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) != BattleType.SINGLE) {
            ArrayList<PixelmonWrapper> targetTeam = target.getTeamPokemon();
            if (user.bc.rules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) == BattleType.DOUBLE) {
                for(PixelmonWrapper pw : targetTeam) {
                    if (pw != target) {
                        newTarget = pw;
                        break;
                    }
                }
            }

            if (user.bc.rules.getOrDefault(BattleRuleRegistry.BATTLE_TYPE) == BattleType.TRIPLE) {
                int newTargetPosition = target.battlePosition;
                if (user.battlePosition == 1) {
                    boolean leftRightTarget = RandomHelper.getRandomChance();
                    if (leftRightTarget) {
                        newTargetPosition = (target.battlePosition + 1) % 3;
                    } else {
                        newTargetPosition = (target.battlePosition + 2) % 3;
                    }
                }

                for(PixelmonWrapper pw : targetTeam) {
                    if (user.battlePosition == 0) {
                        if (pw != target && pw.battlePosition != 2) {
                            newTarget = pw;
                            break;
                        }
                    } else if (user.battlePosition == 2) {
                        if (pw != target && pw.battlePosition != 0) {
                            newTarget = pw;
                            break;
                        }
                    } else if (pw != target) {
                        if (target.getTeamPokemon().size() < 3) {
                            newTarget = pw;
                        } else if (pw.battlePosition == newTargetPosition) {
                            newTarget = pw;
                            break;
                        }
                    }
                }
            }
        }

        return newTarget;
    }
}

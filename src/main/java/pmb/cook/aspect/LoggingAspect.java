package pmb.cook.aspect;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LoggingAspect {

  protected final Logger logger = LogManager.getLogger(getClass());

  private static final String JOINT_POINT = "execution(* *(..)) && !execution(* lambda*(..)) && !execution(@pmb.cook.aspect.NoLogging * *(..)) && !within(@pmb.cook.aspect.NoLogging *)";

  @Before(JOINT_POINT)
  public void logMethodEntry(JoinPoint joinPoint) {

    Object[] args = joinPoint.getArgs();

    Signature signature = joinPoint.getSignature();
    String name = signature.getName();
    String className = signature.getDeclaringTypeName();
    Method method = ((MethodSignature) signature).getMethod();
    String argsToString = "";
    if (!method.isAnnotationPresent(Logging.class) || method.getAnnotation(Logging.class).logArgs()) {
      argsToString = Optional.ofNullable(args).map(Arrays::asList).stream().flatMap(List::stream).map(arg -> {
        if (arg == null) {
          return "null";
        }
        String result = arg.getClass().getSimpleName() + " ";
        if (arg instanceof List<?>) {
          result += "of size: " + ((List<?>) arg).size();
        } else if (arg.getClass().isArray()) {
          result += "of size: " + ((Object[]) arg).length;
        } else {
          result += arg.toString();
        }
        return result;
      }).collect(Collectors.joining(", ", "(", ")"));
      argsToString = StringUtils.equals(argsToString, "()") ? "" : " - called with: " + argsToString;
    }

    if (logger.isDebugEnabled()) {
      logger.debug(MessageFormat.format("Start {0} # {1}{2}", className, name, argsToString));
    }
  }

  @After(JOINT_POINT)
  public void logMethodExit(JoinPoint joinPoint) {
    Signature signature = joinPoint.getSignature();
    String name = signature.getName();
    String className = signature.getDeclaringTypeName();
    if (logger.isDebugEnabled()) {
      logger.debug(MessageFormat.format("End {0} # {1}", className, name));
    }
  }

}

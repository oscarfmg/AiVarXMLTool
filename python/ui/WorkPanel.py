from abc import ABCMeta, abstractmethod

class WorkPanel:
    __metaclass__ = ABCMeta

    @abstractmethod
    def getText(self): raise NotImplementedError
